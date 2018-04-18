package controllers;

import models.*;
import views.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class ControleurMedia {

	private ControleurGeneral ctrGeneral;
	private static MainGUI mainGUI;
	private ORMAccess ormAccess;
	
	private GlobalData globalData;

	public ControleurMedia(ControleurGeneral ctrGeneral, MainGUI mainGUI, ORMAccess ormAccess){
		this.ctrGeneral=ctrGeneral;
		ControleurMedia.mainGUI=mainGUI;
		this.ormAccess=ormAccess;
	}

	public void sendJSONToMedia(){
		new Thread(){
			public void run(){
				mainGUI.setAcknoledgeMessage("Envoi JSON ... WAIT");
				//long currentTime = System.currentTimeMillis();
				try {
					globalData = ormAccess.GET_GLOBAL_DATA();
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plex.json"), "UTF-8"));

					StringBuilder str = new StringBuilder();

					str.append("{\n");
					str.append("\t\"projections\": {\n");
					str.append("\t\t\"projection\": [\n");

					for (Projection projection : globalData.getProjections()){
						// Début d'une projection
						str.append("\t\t\t{\n");

						// Date
						Calendar date = projection.getDateHeure();
						str.append("\t\t\t\t\"date\": {\n");
						str.append("\t\t\t\t\t\"jour\": \"" + date.get(Calendar.DAY_OF_MONTH) +"\",\n");
						str.append("\t\t\t\t\t\"mois\": \"" + date.get(Calendar.MONTH) +"\",\n");
						str.append("\t\t\t\t\t\"annee\": \"" + date.get(Calendar.YEAR) +"\",\n");
						str.append("\t\t\t\t\t\"heure\": \"" + date.get(Calendar.HOUR_OF_DAY) +"\",\n");
						str.append("\t\t\t\t\t\"minute\": \"" + date.get(Calendar.MINUTE) +"\",\n");
						str.append("\t\t\t\t},\n");

						// Film
						Film film = projection.getFilm();
						str.append("\t\t\t\t\"film\": {\n");
						str.append("\t\t\t\t\t\"titre\": \"" + film.getTitre() + "\"\n");



						// Fin d'une projection
						str.append("\t\t\t},\n");
					}

//					str.deleteCharAt(str.)

					// Referme les objets/tableaux ouverts
					str.append("\t\t]\n\t}\n}");

					out.write(str.toString());
				}
				catch (Exception e){
					mainGUI.setErrorMessage("Construction XML impossible", e.toString());
				}
			}
		}.start();
	}

}