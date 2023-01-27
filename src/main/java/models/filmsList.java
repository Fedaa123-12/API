package models;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "films")

public class filmsList {
	@XmlElement(name = "film")
	private List<Film> filmsList;
	public  filmsList(){}
	
	public filmsList(List<Film> filmsList) {
		this.filmsList = filmsList;
	}
	
	public List<Film> getFilmsList() {
		return filmsList;
	}
	
	public void setFilmsList(List<Film> filmsList) {
		this.filmsList = filmsList;
	}
}