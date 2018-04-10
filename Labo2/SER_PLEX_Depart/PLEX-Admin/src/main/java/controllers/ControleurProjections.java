package controllers;


import java.util.*;
import javax.swing.*;

import models.*;
import views.*;

public class ControleurProjections {

	private MainGUI mainGUI;						// Référence à la fenêtre principale
	private ProjectionsGUIMainView projectionsMainView;	// Référence à la vue principale

	private ProjectionsListView listView;					// Référence sur la vue secondaire - Show - Edit -..

	private ORMAccess ormAccess;						// Tampon avec l'ORM Hibernate

	public ControleurProjections(ORMAccess ormAccess, MainGUI mainGUI ){
		this.ormAccess=ormAccess;
		this.mainGUI=mainGUI;
		projectionsMainView = new ProjectionsGUIMainView(this);
		mainGUI.addOnglet("Projections", projectionsMainView);
		listView = projectionsMainView.getListView();
		internalProjectionsListRefresh();
	}

	public void setSecondaryView(JPanel view) {
		projectionsMainView.setSecondaryPanel(view); 	// Afficher la vue secondaire - Show - Edit -..
	}

	// REQUETES EN PROVENANCE DES VUES
	// ------------------------------

	public void cancelEdition(){
		mainGUI.resetMessage();
		setSecondaryView(null);
	}

	public void cancelCreation(){
		mainGUI.resetMessage();
		setSecondaryView(null);
	}

	public void refreshProjectionsList() {
		new Thread(){
			public void run() {
				internalProjectionsListRefresh();
			}
		}.start();

	}

	public void internalProjectionsListRefresh() {
		setSecondaryView(null);
		try {
			List<Projection> projections = ormAccess.GET_PROJECTIONS();	
			listView.resetWith(projections);
			mainGUI.resetMessage();
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Liste des projections non accessible", e.toString());
		}	
	}

	public void addProjection() {
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			List<String> titresFilms = ormAccess.GET_TITRES_FILMS();
			List<String> nosSalles = ormAccess.GET_NOS_DES_SALLES();
			if (titresFilms.size()< 1 || nosSalles.size()< 1 ) {
				mainGUI.setWarningMessage("Creation impossible: pas de film (ou pas de salle) a disposition");
			}
			else {
				ProjectionNewView projectionNewView= new ProjectionNewView(this, titresFilms, nosSalles);
				setSecondaryView(projectionNewView);
			}
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Probleme rencontre: ", e.toString());
		}	
	}

	public void editProjection(long projectionId) {
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			Projection projection = ormAccess.GET_PROJECTION(projectionId); 
			List<String> titresFilms = ormAccess.GET_TITRES_FILMS();
			List<String> nosSalles = ormAccess.GET_NOS_DES_SALLES();
			ProjectionEditView projectionEditView= new ProjectionEditView(this, projection, titresFilms, nosSalles);
			setSecondaryView(projectionEditView);
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Projection "+projectionId+ " non accessible", e.toString());
		}		
	}



	public void showProjection(long projectionId) {
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			Projection projection = ormAccess.GET_PROJECTION(projectionId); 
			ProjectionShowView projectionShowView= new ProjectionShowView(this, projection);
			setSecondaryView(projectionShowView);
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Projection "+projectionId+ " non accessible", e.toString());
		}	
	}

	public void newProjection (Calendar dateChoisie, String titreFilm, String noSalle){
		mainGUI.resetMessage();
		setSecondaryView(null);
		try {
			Projection projection = new Projection(
					ormAccess.GET_FILM_AVEC_TITRE(titreFilm),
					ormAccess.GET_SALLE(noSalle),
					dateChoisie);
			ormAccess.SAVE_PROJECTION(projection);
			internalProjectionsListRefresh();
			mainGUI.setAcknoledgeMessage("Creation projection OK");
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Enregistrement impossible", e.toString());
		}	
	}

	public void updateProjection (Projection projection, Calendar dateChoisie, String titreFilm, String noSalle){
		mainGUI.resetMessage();
		try {
			Salle ancienneSalle = projection.getSalle();
			Film ancienFilm = projection.getFilm();
			ancienneSalle.getProjections().remove(projection);
			ancienFilm.getProjections().remove(projection);

			Salle nouvelleSalle = ormAccess.GET_SALLE(noSalle);
			Film nouveauFilm = ormAccess.GET_FILM_AVEC_TITRE(titreFilm);
			projection.setFilm(nouveauFilm);
			projection.setSalle(nouvelleSalle);
			projection.setDateHeure(dateChoisie);
			nouvelleSalle.getProjections().add(projection);
			nouveauFilm.getProjections().add(projection);

			ormAccess.UPDATE_PROJECTION(projection);
			internalProjectionsListRefresh();
			mainGUI.setAcknoledgeMessage("Mise a jour projection OK");
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Mise a jour impossible", e.toString());
		}
	}

	@SuppressWarnings("unused")
	public void deleteProjection(long projectionId) {
		mainGUI.resetMessage();
		try {
			Projection projection = ormAccess.DELETE_PROJECTION(projectionId); 
			internalProjectionsListRefresh();
		}
		catch (Exception e){
			mainGUI.setErrorMessage("Projection "+projectionId+ " non accessible", e.toString());
		}	
		mainGUI.setAcknoledgeMessage("Suppression projection OK");
	}

}