package controllers;

import views.*;

import ch.heigvd.iict.ser.imdb.models.*;

// Pour XStream
import com.thoughtworks.xstream.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

public class ControleurGeneral {

	private static MainGUI mainGUI;
	private static ORMAccess ormAccess;
	private ControleurProjections ctrProjections; 
	private ControleurFilms ctrFilms;

	//private ControleurWFC ctrWFC;
	private ControleurMedia ctrMedia; 
	private ControleurXMLCreation ctrXMLCreation;

	public ControleurGeneral(ORMAccess ormAccess){
		ControleurGeneral.ormAccess = ormAccess;
		mainGUI = new MainGUI(this);
		ctrProjections = new ControleurProjections(ormAccess, mainGUI);
		ctrFilms = new ControleurFilms(ormAccess, mainGUI);
		ctrProjections.refreshProjectionsList();
		ctrFilms.refreshFilmsList();

		ctrMedia = new ControleurMedia(this, mainGUI, ormAccess);
		ctrXMLCreation = new ControleurXMLCreation(this, mainGUI, ormAccess);
	}

	public void createXStreamXML(){ctrXMLCreation.createXStreamXML();}

	public void createXML(){ctrXMLCreation.createXML();}

	public void sendJSONToMedia(){ctrMedia.sendJSONToMedia();}

	public void initBaseDeDonnees() {
		new Thread(){
			public void run() {
				mainGUI.disableButtons();
				try {
					Data data = donneesDuFichierWFC_XML();

					ormAccess.initBD(data, mainGUI);

					ctrProjections.refreshProjectionsList();
					ctrFilms.refreshFilmsList();
					mainGUI.resetMessage();			
				}
				catch (Exception e ) {
					mainGUI.setErrorMessage("Erreur peuplement BD", e.toString());
				} 
				finally {
					mainGUI.enableButtons();
				}
			}
		}.start();
	}
	
	public void initBaseDeDonneesFromWFC(Data data) {
		final Data fData = data;
		new Thread(){
			public void run() {
				mainGUI.disableButtons();
				try {
					ormAccess.initBD(fData, mainGUI);

					ctrProjections.refreshProjectionsList();
					ctrFilms.refreshFilmsList();
					mainGUI.resetMessage();			
				}
				catch (Exception e ) {
					mainGUI.setErrorMessage("Erreur peuplement BD", e.toString());
				} 
				finally {
					mainGUI.enableButtons();
				}
			}
		}.start();
	}

	public void initBaseDeDonneesAvecNouvelleVersion(final Data data) {
		new Thread(){
			public void run() {
				mainGUI.disableButtons();
				mainGUI.setAcknoledgeMessage("Init BD");
				try {
					ormAccess.initBDAvecNouvellesVersionWFC(data, mainGUI);

					ctrProjections.refreshProjectionsList();
					ctrFilms.refreshFilmsList();
					mainGUI.resetMessage();			
				}
				catch (Exception e ) {
					mainGUI.setErrorMessage("Erreur peuplement BD", e.toString());
				} 
				finally {
					mainGUI.enableButtons();
				}
			}
		}.start();
	}

	@SuppressWarnings("unchecked")
	public Data donneesDuFichierWFC_XML() throws Exception {
		// Désérialisation du fichier XStream
		XStream xstream = new XStream();
		System.out.println("Chargement du contenu XML... ");
		mainGUI.setAcknoledgeMessage("Chargement du contenu XML... ");
		Data data = new Data();
		java.net.URL urlMoviesFile = ORMAccess.class.getResource("/movies_v1.xml");
		java.net.URL urlPersonsFile = ORMAccess.class.getResource("/persons_v1.xml");
		data.setPersons((Map<Long, Person>)readFromFile(urlPersonsFile.getPath(), xstream));
		data.setMovies((Map<Long, Movie>)readFromFile(urlMoviesFile.getPath(), xstream));
		System.out.println("Chargement du contenu XML: OK");
		return data;
	}

	private Object readFromFile(String filename, XStream serializer) {
		Object result = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			result = serializer.fromXML(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isAlive() { return mainGUI != null; }
}