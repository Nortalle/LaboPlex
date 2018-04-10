package models;

import java.util.*;
import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "projections")
public class Projection  implements Serializable {

	private static final long serialVersionUID = -5502829643430355820L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public long getId() {return id;}

	@Column(name="date_heure", nullable = true, length = 40)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateHeure;
	public Calendar getDateHeure(){return dateHeure;}
	public void setDateHeure(Calendar dateHeure){
		this.dateHeure=dateHeure;	
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "film_id", nullable = true)
	private Film film;
	public Film getFilm() {return film;}
	public void setFilm(Film film) { this.film = film; }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salle_id", nullable = true)
	private Salle salle;
	public Salle getSalle() {return salle;}
	public void setSalle(Salle salle) {this.salle = salle;}

	public Projection() {}	
	
	public Projection (Film film, Salle salle, Calendar dateHeure){
		this.setFilm(film);
		this.setSalle(salle);
		salle.getProjections().add(this);
		film.getProjections().add(this);
		this.setDateHeure(dateHeure);
	}

	public String toString(){         
 		return "Projection (" + "Film: " + film.getTitre() + "Salle: " + salle.getNo() +")";
 	}	
	 	 	 
}