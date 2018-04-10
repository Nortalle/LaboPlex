package controllers;

import java.util.*;
import javax.swing.*;

import models.*;
import views.*;

public class ControleurFilms {

	private MainGUI mainGUI;						// Référence à la fenêtre principale
	private FilmsGUIMainView filmsMainView;			// Référence à la vue principale

	private FilmsListView listView;					// Référence sur la vue "Liste des films"
	//private JPanel secondaryView;					// Référence sur la vue secondaire - Show - Edit -..

	private ORMAccess ormAccess;					// Tampon avec l'ORM Hibernate
	//private SessionFactory sessionFactory;

	public ControleurFilms(ORMAccess ormAccess, MainGUI mainGUI ){
		this.ormAccess=ormAccess;
		this.mainGUI=mainGUI;
		filmsMainView = new FilmsGUIMainView(this);
		mainGUI.addOnglet("Films", filmsMainView);
		listView = filmsMainView.getListView();
		internalFilmsListRefresh();
	}

	public void setSecondaryView(JPanel view) {
		//secondaryView = view;
		filmsMainView.setSecondaryPanel(view); 	// Afficher la vue secondaire - Show - Edit -..
	}

	// REQUETES EN PROVENANCE DES VUES
	// ------------------------------

	public void cancelPhotoEdition(){
		mainGUI.resetMessage();
		setSecondaryView(null);
	}

	public void cancelCritiqueCreation(){
		mainGUI.resetMessage();
		setSecondaryView(null);
	}

	public void refreshFilmsList() {
		internalFilmsListRefresh();
	}

	public void internalFilmsListRefresh() {
		setSecondaryView(null);
		try {
			List<Film> films = ormAccess.GET_FILMS();	
			listView.resetWith(films);
			mainGUI.resetMessage();
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Liste des films non accessible", e.toString());
		}	
	}

	public void addCritique(long filmId) {
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			Film film = ormAccess.GET_FILM(filmId); 
			CritiqueNewView critiqueNewView= new CritiqueNewView(this, film);
			setSecondaryView(critiqueNewView);
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Probleme rencontre: ", e.toString());
		}	
	}

	public void editPhoto(long filmId) {
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			Film film = ormAccess.GET_FILM(filmId); 
			FilmEditView filmEditView= new FilmEditView(this, film);
			setSecondaryView(filmEditView);
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Film "+filmId+ " non accessible", e.toString());
		}		
	}

	public void showFilm(long filmId) {
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			Film film = ormAccess.GET_FILM(filmId); 
			FilmShowView filmShowView= new FilmShowView(this, film);
			setSecondaryView(filmShowView);
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Film "+filmId+ " non accessible", e.toString());
		}	
	}

	public void newCritique (Film film, int note, String critique){
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			
			Critique nouvelle_critique = new Critique(note, critique);
			film.ajouterCritique(nouvelle_critique);
			ormAccess.UPDATE_FILM(film);
			
			internalFilmsListRefresh();
			mainGUI.setAcknoledgeMessage("Ajout critique OK" );
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Enregistrement impossible", e.toString());
		}	
	}

	public void updateFilm (Film film, String pathPhoto){
		mainGUI.resetMessage();
		try {
			film.setPhoto(pathPhoto);
			ormAccess.UPDATE_FILM(film);
			internalFilmsListRefresh();
			mainGUI.setAcknoledgeMessage("Mise a jour Photo OK" );
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Mise a jour impossible", e.toString());
		}
	}


}