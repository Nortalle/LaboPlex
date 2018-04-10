package models;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "critiques")
public class Critique implements Serializable {

	private static final long serialVersionUID = 1774238086139787018L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public long getId() {return id;}

	@Column(name="note", nullable = true)
	private int note;
	public int getNote(){return note;}
	public void setNote(int note){
		this.note=note;	
	}

	@Column(name="texte", nullable = true, columnDefinition = "TEXT")
	private String texte;
	public String getTexte(){return texte;}
	public void setTexte(String texte){
		this.texte=texte;	
	}

 	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "film_id", nullable = true)
	private Film film;
	public Film getFilm() {return film;}
	public void setFilm(Film film) {this.film = film;}


	public Critique() {}	
	
	public Critique(int note, String text){
		this.note = note;
		this.setTexte(text);
	}
	 	 	 
}