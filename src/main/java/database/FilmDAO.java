package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import models.Film;

public class FilmDAO {
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "chamatfe";
    String password = "worgFlew3";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;
    
    
    private static FilmDAO FilmDAO;
	private FilmDAO() {}
	//Adds a way for servlets to instantiate singletons
	public static FilmDAO getInstance() {
		if(null == FilmDAO) {
			FilmDAO = new FilmDAO();
		}
		
		return FilmDAO;
	}

	public void getUser() {
		System.out.println(user + password + " " + url);
	}
	
	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }	   
    }
	
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	public ArrayList<Film> getAllFilms(){
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films ORDER BY id DESC";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }
   
	public ArrayList<Film> getFilmBySearch(String input,String searchBy){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films WHERE " + searchBy +" LIKE '%" + input + "%'" ;
		   
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
  }

	public Film getFilmByID(int id){
	   
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }
   
	public void updateFilm(Film f) throws SQLException {
	   try {
			String updateSQL = "UPDATE films SET title = '"+f.getTitle()+"', year = "+f.getYear()+", director = '"+f.getDirector()+ "', stars = '"+f.getStars()+"', review = '"+f.getReview()+"' WHERE id ="+f.getId()+";";
			
			openConnection();
			stmt.executeUpdate(updateSQL);
			stmt.close();
			closeConnection();
		} catch (SQLException s) {
			throw new SQLException("Contact Not Added");
		} 	
   }
   
	public void deleteFilm(int id) throws SQLException {
	String deleteSQL = "delete from films where id="+id;
	try{
		openConnection();
		stmt.executeUpdate(deleteSQL);
		stmt.close();
		closeConnection();
	}
	catch(SQLException se) { System.out.println(se); }	
}
	
	public void insertContact(Film f) throws SQLException {


		try {
			openConnection();
			String insertSql = "insert into films (title, year, director, stars, review) values ('" + f.getTitle() + "','" + f.getYear() + "','" + f.getDirector() + "','" + f.getStars()+ "','" + f.getReview() +"');";
			
			stmt.executeUpdate(insertSql);
			stmt.close();
			closeConnection();
		} catch (SQLException s) {
			throw new SQLException("Contact Not Added");
		}
	}
   
}
