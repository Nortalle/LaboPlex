package ch.heigvd.iict.ser.imdb.models;

import java.io.Serializable;
import java.util.Map;

public class Data implements Serializable {

	private static final long serialVersionUID = -7676157349813018600L;
	
	private long 				version = -1L;
	private Map<Long, Movie> 	movies 	= null;
	private Map<Long, Person> 	persons = null;
	
	public long getVersion() { return version; }
	public void setVersion(long version) { this.version = version; }
	
	public Map<Long, Movie> getMovies() { return movies; }
	public void setMovies(Map<Long, Movie> movies) { this.movies = movies; }
	
	public Map<Long, Person> getPersons() { return persons; }
	public void setPersons(Map<Long, Person> persons) { this.persons = persons; }
	
}
