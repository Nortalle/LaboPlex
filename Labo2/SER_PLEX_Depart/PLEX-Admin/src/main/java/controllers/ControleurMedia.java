package controllers;

import models.*;
import views.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import com.google.gson.*;

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
				try {
					globalData = ormAccess.GET_GLOBAL_DATA();

					Gson moteurJson = new GsonBuilder().setPrettyPrinting().create();

					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plex.json"), "UTF-8"));
					JsonObject data = new JsonObject();
					JsonArray projectionsList = new JsonArray();

					for (Projection proj : globalData.getProjections()){
						JsonObject projection = new JsonObject();

						// Date
						JsonObject date = new JsonObject();
						Calendar projectionDate = proj.getDateHeure();
						date.add("jour", new JsonPrimitive(projectionDate.get(Calendar.DAY_OF_MONTH)));
						date.add("mois", new JsonPrimitive(projectionDate.get(Calendar.MONTH)));
						date.add("annee", new JsonPrimitive(projectionDate.get(Calendar.YEAR)));
						date.add("heure", new JsonPrimitive(projectionDate.get(Calendar.HOUR_OF_DAY)));
						date.add("minute", new JsonPrimitive(projectionDate.get(Calendar.MINUTE)));
						projection.add("date", date);

						// Film
						JsonObject film = new JsonObject();
						film.add("titre", new JsonPrimitive(proj.getFilm().getTitre()));

						// Acteur
						JsonArray actorList = new JsonArray();
						for(RoleActeur role : proj.getFilm().getRoles()){
							if(role.getPlace() == 1 || role.getPlace() == 2){
								JsonObject actor = new JsonObject();
								if(role.getActeur().getNomNaissance() != null)
									actor.add("nom_naissance", new JsonPrimitive(role.getActeur().getNomNaissance()));
								else
									actor.add("nom", new JsonPrimitive(role.getActeur().getNom()));

								actor.add("role", new JsonPrimitive((role.getPersonnage())));

								actorList.add(actor);
							}

						}

						film.add("acteurs", actorList);
						projection.add("film", film);

						projectionsList.add(projection);
					}

					data.add("projections", projectionsList);

					out.write(moteurJson.toJson(data));
					out.close();

					mainGUI.setAcknoledgeMessage("Envoi JSON ... FINISHED");
				}
				catch (Exception e){
					mainGUI.setErrorMessage("Construction XML impossible", e.toString());
				}
			}
		}.start();
	}

}