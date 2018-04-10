package models;

import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;

@Entity
@Table(name = "salles")
public class Salle implements Serializable {

	private static final long serialVersionUID = -4585652901166240140L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public long getId() {return id;}

	@Column(name="no", nullable = true, length = 40)
	private String no;
	public String getNo(){return no;}
	public void setNo(String no){
		this.no=no;	
	}

	@Column(name="taille", nullable = true, length = 40)
	private int taille;
	public int getTaille(){return taille;}
	public void setTaille(int taille){
		this.taille=taille;	
	}

	@OneToMany(targetEntity=Projection.class, 
		fetch = FetchType.EAGER, 
		mappedBy="salle")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<Projection> projections = new HashSet<Projection>();
	public Set<Projection> getProjections() { return projections;} 
 	public void setProjections(Set<Projection> projections) {
 	 	this.projections = projections;
 	}

	public Salle() {}	
	
	public Salle (int taille, String no){
		setTaille(taille);
		setNo(no);
	}

	public String toString(){         
 		return "Salle (" + "No: " + this.getNo() + ")";
 	}	

}