package ch.heigvd.iict.ser.imdb.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Person implements Serializable {
	
	private static final long serialVersionUID = -200642918131520760L;
	
	public enum Gender {
		MALE,
		FEMALE,
		UNKNOWN
	}

	public final static SimpleDateFormat[] DATE_FORMATS = new SimpleDateFormat[]{
		new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH),
		new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH),
		new SimpleDateFormat("yyyy", Locale.ENGLISH),
		new SimpleDateFormat("'c.' yyyy", Locale.ENGLISH),
	};
	
	private long 			id					= -1L;
	private String 			name 				= null;
	private List<String> 	akaNames 			= null;
	private Gender 			gender 				= Gender.UNKNOWN;
	private String 			birthDateAsString 	= null;
	private String 			deathDateAsString 	= null;
	private String 			birthName			= null;
	private String 			biography			= null;
	private String 			image				= null;
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public List<String> getAkaNames() { return akaNames; }
	public void setAkaNames(List<String> akaNames) { this.akaNames = akaNames; }
	
	public Gender getGender() { return gender; }
	public void setGender(Gender gender) { this.gender = gender; }
	
	public String getBirthDateAsString() { return birthDateAsString; }
	public void setBirthDateAsString(String birthDateAsString) { this.birthDateAsString = birthDateAsString; }
	
	public String getDeathDateAsString() { return deathDateAsString; }
	public void setDeathDateAsString(String deathDateAsString) { this.deathDateAsString = deathDateAsString; }
	
	public String getBirthName() { return birthName; }
	public void setBirthName(String birthName) { this.birthName = birthName; }
	
	public String getBiography() { return biography; }
	public void setBiography(String biography) { this.biography = biography; }
	
	public String getImage() { return image; }
	public void setImage(String image) { this.image = image; }
	
	public Date getBirthDate() {
		if(this.birthDateAsString == null) return null;
		for(SimpleDateFormat format : DATE_FORMATS) {
			try{
				return format.parse(this.birthDateAsString);
			} catch(Exception e) {}
		}
		return null;
	}
	
	public Date getDeathDate() {
		if(this.deathDateAsString == null) return null;
		for(SimpleDateFormat format : DATE_FORMATS) {
			try{
				return format.parse(this.deathDateAsString);
			} catch(Exception e) {}
		}
		return null;
	}
	
}

