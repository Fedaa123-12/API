package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import database.FilmDAO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import models.Film;
import models.filmsList;

/**
 * Servlet implementation class filmsController
 */
@WebServlet("/filmsController")
public class filmsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();
	FilmDAO dao = FilmDAO.getInstance();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public filmsController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String format = request.getHeader("Accept");
		String idRequest = request.getParameter("id");
		String searchBy = request.getParameter("searchBy");
		String searchinput = request.getParameter("searchinput");
		PrintWriter out = response.getWriter();
		StringWriter sw = new StringWriter();
		ArrayList<Film> allFilms;

		// send the film by the given id through the request
		if (idRequest != null) {
			response.setContentType("text/plain");
			int paramInt = Integer.valueOf(idRequest);
			Film film = dao.getFilmByID(paramInt);
			String title = film.getTitle();
			int year = film.getYear();
			String director = film.getDirector();
			String stars = film.getStars();
			String review = film.getReview();
			out.write(title + "#" + year + "#" + director + "#" + stars + "#" + review);
			out.close();
		}
		// send data back
		else {
			// check if its a request for a search or a full film request
			if (searchBy != null && searchinput != null && searchBy != "" && searchinput != "") {
				allFilms = dao.getFilmBySearch(searchinput, searchBy);
			} else {
				allFilms = dao.getAllFilms();
			}

			//send back the data to the request with the requested format
			if (("application/json").equals(format) || ("*/*".equals(format))) {
				response.setContentType("application/json");
				String json = gson.toJson(allFilms);
				out.write(json);
				out.close();
			} 
			else if (("application/xml").equals(format)) {
				response.setContentType("text/xml");
				filmsList fl = new filmsList(allFilms);
				JAXBContext context;
				try {
					context = JAXBContext.newInstance(filmsList.class);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(fl, sw);
					out.println(sw.toString());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else if (("text/plain").equals(format)) {
				response.setContentType("text/plain");
				for (int i = 0; i < allFilms.size(); i++) {
					int id = allFilms.get(i).getId();
					String title = allFilms.get(i).getTitle();
					int year = allFilms.get(i).getYear();
					String director = allFilms.get(i).getDirector();
					String stars = allFilms.get(i).getStars();
					String review = allFilms.get(i).getReview();
					out.write("$" + id + "#" + title + "#" + year + "#" 
					+ director + "#" + stars + "#" + review);
				}
			}
		}
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		String dataFormat = request.getHeader("Content-Type");
		PrintWriter out = response.getWriter();
		
		//get back the data to the request with the requested format
		if (dataFormat.equals("application/json") || ("*/*".equals(dataFormat))) {
			Film F = gson.fromJson(data, Film.class);
			String title = F.getTitle();
			int year = F.getYear();
			String director = F.getDirector();
			String stars = F.getStars();
			String review = F.getReview();
			Film film = new Film(title, year, director, stars, review);
			try {
				dao.insertContact(film);
				out.println("film added");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (dataFormat.equals("application/xml")) {
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				String title = f.getTitle();
				int year = f.getYear();
				String director = f.getDirector();
				String stars = f.getStars();
				String review = f.getReview();
				Film film = new Film(title, year, director, stars, review);
				dao.insertContact(film);
				out.println("film added");

			} catch (JAXBException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (dataFormat.equals("text/plain")) {
			String sendData[] = data.split("#");
			String title = sendData[0];
			int year = Integer.valueOf(sendData[1]);
			String director = sendData[2];
			String stars = sendData[3];
			String review = sendData[4];
			Film film = new Film(title, year, director, stars, review);
			try {
				dao.insertContact(film);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("film added");
		}
	}

	
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dataFormat = request.getHeader("Content-Type");
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		int id;
		PrintWriter out = response.getWriter();
		
		//get back the data to the request with the requested format
		if (dataFormat.equals("application/json") || ("*/*".equals(dataFormat))) {
			Film F = gson.fromJson(data, Film.class);
			id = F.getId();
			String title = F.getTitle();
			int year = F.getYear();
			String director = F.getDirector();
			String stars = F.getStars();
			String review = F.getReview();
			Film film = new Film(id, title, year, director, stars, review);
			try {
				dao.updateFilm(film);
				out.println("film updated");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (dataFormat.equals("application/xml")) {
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				id = f.getId();
				String title = f.getTitle();
				int year = f.getYear();
				String director = f.getDirector();
				String stars = f.getStars();
				String review = f.getReview();

				Film film = new Film(id, title, year, director, stars, review);
				dao.updateFilm(film);
				out.println("film updated");
			} catch (JAXBException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (dataFormat.equals("text/plain")) {
			String sendData[] = data.split("#");
			id = Integer.valueOf(sendData[0]);
			String title = sendData[1];
			int year = Integer.valueOf(sendData[2]);
			String director = sendData[3];
			String stars = sendData[4];
			String review = sendData[5];

			Film film = new Film(id, title, year, director, stars, review);
			try {
				dao.updateFilm(film);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("film updated");
		}
	}

	
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dataFormat = request.getHeader("Content-Type");
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		int id;
		PrintWriter out = response.getWriter();
		
		//get back the data to the request with the requested format
		if (dataFormat.equals("application/json") || ("*/*".equals(dataFormat))) {
			Film F = gson.fromJson(data, Film.class);
			id = F.getId();
			try {
				dao.deleteFilm(id);
				out.println("film deleted");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (dataFormat.equals("application/xml")) {
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film f = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				id = f.getId();
				dao.deleteFilm(id);
				out.println("film deleted");

			} catch (JAXBException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (dataFormat.equals("text/plain")) {
			String sendData[] = data.split("#");
			id = Integer.valueOf(sendData[0]);
			try {
				dao.deleteFilm(id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("film Deleted");
		}
	}
}