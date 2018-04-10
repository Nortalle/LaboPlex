package models;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "roles")
public class RoleActeur implements Serializable {

	private static final long serialVersionUID = -3385617263988459551L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public long getId() {return id;}

	@Column(name="place", nullable = true, length = 40)
	private long place;
	public long getPlace(){return place;}
	public void setPlace(long place){
		this.place=place;	
	}

	@Column(name="personnage", nullable = true, length = 80)
	private String personnage;
	public String getPersonnage(){return personnage;}
	public void setPersonnage(String personnage){
		this.personnage=personnage;	
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "film_id", nullable = true)
	private Film film;
	public Film getFilm() {return film;}
	public void setFilm(Film film) { this.film = film; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "acteur_id", nullable = true)
	private Acteur acteur;
	public Acteur getActeur() {return acteur;}
	public void setActeur(Acteur acteur) {this.acteur = acteur;}

	public RoleActeur() {
	}	
	
	public RoleActeur (Acteur acteur, long place, String personnage){
		this.setActeur(acteur);
		acteur.getRoles().add(this);
		this.setPlace(place);
		this.setPersonnage(personnage);
	}

	public String toString(){         
 		return "Role (" + "Film: " + film.getTitre() + "Acteur: " + acteur.getNom() +")";
 	}	 
}