package ch.heigvd.iict.ser.imdb.models;

import java.io.Serializable;
import java.util.Set;

public class Movie  implements Serializable {

	private static final long serialVersionUID = 5382858186892929803L;

	private long 		id				= -1L;
	private String 		title 			= null;
	private String 		plot 			= null;
	private Set<String> languages 		= null;
	private String 		budget 			= null;
	private Set<String> genres 			= null;
	private Set<String> keywords 		= null;
	private int			runtime			= -1;
	
	private Set<Role> 	actorsIds 		= null;
	private Set<Long> 	directorsIds 	= null;
	private Set<Long> 	editorsIds 		= null;
	private Set<Long> 	producersIds 	= null;
	private Set<Long> 	writersIds 		= null;
	private String 		image			= null;
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getPlot() { return plot; }
	public void setPlot(String plot) { this.plot = plot; }

	public Set<String> getLanguages() { return languages; }
	public void setLanguages(Set<String> languages) { this.languages = languages; }
	
	public String getBudget() { return budget; }
	public void setBudget(String budget) { this.budget = budget; }
	
	public Set<String> getGenres() { return genres; }
	public void setGenres(Set<String> genres) { this.genres = genres; }
	
	public Set<String> getKeywords() { return keywords; }
	public void setKeywords(Set<String> keywords) { this.keywords = keywords; }
	
	public int getRuntime() { return runtime; }
	public void setRuntime(int runtime) { this.runtime = runtime; }
	
	public String getImage() { return image; }
	public void setImage(String image) { this.image = image; }
	
	public Set<Role> getActorsIds() { return actorsIds; }
	public void setActorsIds(Set<Role> actorsIds) { this.actorsIds = actorsIds; }
	
	public Set<Long> getDirectorsIds() { return directorsIds; }
	public void setDirectorsIds(Set<Long> directorsIds) { this.directorsIds = directorsIds; }
	
	public Set<Long> getEditorsIds() { return editorsIds; }
	public void setEditorsIds(Set<Long> editorsIds) { this.editorsIds = editorsIds; }
	
	public Set<Long> getProducersIds() { return producersIds; }
	public void setProducersIds(Set<Long> producersIds) { this.producersIds = producersIds; }
	
	public Set<Long> getWritersIds() { return writersIds; }
	public void setWritersIds(Set<Long> writersIds) { this.writersIds = writersIds; }
	
}
