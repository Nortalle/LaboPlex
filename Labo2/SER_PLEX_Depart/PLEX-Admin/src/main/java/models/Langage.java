package models;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "langages")
public class Langage implements Serializable {

	private static final long serialVersionUID = -7605222452293771054L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public long getId() {return id;}

	@Column(name="label", nullable = true)
	private String label;
	public String getLabel(){return label;}
	public void setLabel(String label){
		this.label=label;	
	}

 	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "film_id", nullable = true)
	private Film film;
	public Film getFilm() {return film;}
	public void setFilm(Film film) {this.film = film;}


	public Langage() {}	
	
	public Langage(String label){
		this.setLabel(label);
	}
	 	 	 
}