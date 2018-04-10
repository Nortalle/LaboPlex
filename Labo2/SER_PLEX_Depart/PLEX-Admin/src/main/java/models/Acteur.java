package models;

import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;


@Entity
@Table(name = "acteurs")
public class Acteur implements Serializable {

	private static final long serialVersionUID = -7494940591783142675L;
	
	@Id
	@Column(name="id")
	private long id;
	public long getId() {return id;}
	public void setId(long identifiant) {
		id = identifiant;
	} 

	@Column(name="nom", nullable = true, length = 80)
	private String nom;
	public String getNom(){return nom;}
	public void setNom(String nom){
		this.nom=nom;	
	}

	@Column(name="nom_naissance", nullable = true, length = 80)
	private String nomNaissance;
	public String getNomNaissance(){return nomNaissance;}
	public void setNomNaissance(String nomNaissance){
		this.nomNaissance=nomNaissance;	
	}

	@Column(name="sexe", nullable = true, length = 40)
	@Enumerated(EnumType.STRING)
	private Sexe sexe;
	public Sexe getSexe(){return sexe;}
	public void setSexe(Sexe sexe){
		this.sexe=sexe;	
	}

	@Column(name="date_naissance", nullable = true, length = 40)
	@Temporal(TemporalType.DATE)
	private Calendar dateNaissance;
	public Calendar getDateNaissance(){return dateNaissance;}
	public void setDateNaissance(Calendar dateNaissance){
		this.dateNaissance=dateNaissance;	
	}

	@Column(name="date_deces", nullable = true, length = 40)
	@Temporal(TemporalType.DATE)
	private Calendar dateDeces;
	public Calendar getDateDeces(){return dateDeces;}
	public void setDateDeces(Calendar dateDeces){
		this.dateDeces=dateDeces;	
	}


	@Column(name="biographie", nullable = true, columnDefinition = "TEXT")
	private String biographie;
	public String getBiographie(){return biographie;}
	public void setBiographie(String biographie){
		this.biographie=biographie;	
	}

	@OneToMany(targetEntity=RoleActeur.class, 
		fetch = FetchType.LAZY,
		mappedBy="acteur")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<RoleActeur> roles = new HashSet<RoleActeur>();
	public Set<RoleActeur> getRoles() { return roles;} 
 	public void setRoles(Set<RoleActeur> roles) {
 	 	this.roles = roles;
 	}

	public Acteur() {}	
	
	public Acteur(String nom, String nomNaissance,
					Sexe sexe, Calendar dateNaissance, Calendar dateDeces,
					String biographie){
		this.nom=nom;
		this.nomNaissance=nomNaissance;
		this.sexe=sexe;
		this.dateNaissance=dateNaissance;
		this.dateDeces=dateDeces;
		this.biographie=biographie;
	}

 
	public String toString(){         
 		return "Acteur (" + "Nom: " + this.getNom() + ")";
 	}	
	 	 	 
}