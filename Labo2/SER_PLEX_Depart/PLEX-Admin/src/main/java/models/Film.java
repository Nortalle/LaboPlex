package models;

import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import java.text.DecimalFormat;

import java.io.Serializable;

@Entity
@Table(name = "films")
public class Film implements Serializable{

	private static final long serialVersionUID = -9141215755281208372L;
	
	@Id
	@Column(name="id")
	private long id;
	public long getId() {return id;}
	public void setId(long identifiant) {
		id = identifiant;
	} 

	@Column(name="titre", nullable = true, length = 80)
	private String titre;
	public String getTitre(){return titre;}
	public void setTitre(String titre){
		this.titre=titre;	
	}

	@Column(name="synopsis", nullable = true, columnDefinition = "TEXT")
	private String synopsis;
	public String getSynopsis(){return synopsis;}
	public void setSynopsis(String synopsis){
		this.synopsis=synopsis;	
	}

	@Column(name="duree", nullable = true, length = 40)
	private int duree;
	public int getDuree(){return duree;}
	public void setDuree(int duree){
		this.duree=duree;	
	}

	@Column(name="photo", nullable = true, length = 255)
	private String photo;
	public String getPhoto(){return photo;}
	public void setPhoto(String photo){
		this.photo=photo;	
	}


	@OneToMany(targetEntity=RoleActeur.class, 
		fetch = FetchType.LAZY,
		mappedBy="film")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<RoleActeur> roles = new HashSet<RoleActeur>();
	public Set<RoleActeur> getRoles() { return roles;} 
 	public void setRoles(Set<RoleActeur> roles) {
 	 	this.roles = roles;
 	}

 	@OneToMany(targetEntity=Projection.class, 
		fetch = FetchType.EAGER,
		mappedBy="film")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<Projection> projections = new HashSet<Projection>();
	public Set<Projection> getProjections() { return projections;} 
 	public void setProjections(Set<Projection> projections) {
 	 	this.projections = projections;
 	}

 	@OneToMany(targetEntity=Critique.class, 
		fetch = FetchType.EAGER, 
		mappedBy="film")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<Critique> critiques = new HashSet<Critique>();
	public Set<Critique> getCritiques() { return critiques;} 
 	public void setCritiques(Set<Critique> critiques) {
 	 	this.critiques = critiques;
 	}

 	@OneToMany(targetEntity=Genre.class, 
		fetch = FetchType.LAZY,
		mappedBy="film")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<Genre> genres = new HashSet<Genre>();
	public Set<Genre> getGenres() { return genres;} 
 	public void setGenres(Set<Genre> genres) {
 	 	this.genres = genres;
 	}

 	@OneToMany(targetEntity=Motcle.class, 
		fetch = FetchType.LAZY,
		mappedBy="film")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<Motcle> motcles = new HashSet<Motcle>();
	public Set<Motcle> getMotcles() { return motcles;} 
 	public void setMotcles(Set<Motcle> motcles) {
 	 	this.motcles = motcles;
 	}

 	@OneToMany(targetEntity=Langage.class, 
		fetch = FetchType.LAZY,
		mappedBy="film")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<Langage> langages = new HashSet<Langage>();
	public Set<Langage> getLangages() { return langages;} 
 	public void setLangages(Set<Langage> langages) {
 	 	this.langages = langages;
 	}

	public Film() {}	
	
	public Film (String titre, String synopsis, int duree){
		this.setTitre(titre);
		this.setSynopsis(synopsis);
		this.setDuree(duree);
	}

	public void ajouterRole(RoleActeur role){
		roles.add(role);
		role.setFilm(this);
	}

	public void ajouterCritique(Critique critique) {
		critiques.add(critique);
		critique.setFilm(this);
	}

	public void ajouterGenre(Genre genre) {
		genres.add(genre);
		genre.setFilm(this);
	}

	public void ajouterMotcle(Motcle motcle) {
		motcles.add(motcle);
		motcle.setFilm(this);
	}

	public void ajouterLangage(Langage langage) {
		langages.add(langage);
		langage.setFilm(this);
	}

	private static final DecimalFormat oneDigitFormat = new DecimalFormat("#.#");

	public String getNoteMoyenne() {
		String resultat;
		double note=0;
		int nb_critiques = this.getCritiques().size();
		if (nb_critiques > 0){
			for (Critique uneCritique: this.getCritiques()){
				note += uneCritique.getNote();
	
			}
			note = note / nb_critiques;
			resultat = oneDigitFormat.format(note);
		}
		else resultat = "-" ;
		return resultat;
	}	


	public String toString(){         
 		return "Film (" + "Titre: " + this.getTitre() + ")";
 	}	
	 	 	 
}