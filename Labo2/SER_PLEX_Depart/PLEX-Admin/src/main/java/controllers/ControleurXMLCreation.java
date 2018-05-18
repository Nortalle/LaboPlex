package controllers;

import ch.heigvd.iict.ser.imdb.models.Role;
import models.*;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import views.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.thoughtworks.xstream.XStream;

public class ControleurXMLCreation {

    //private ControleurGeneral ctrGeneral;
    private static MainGUI mainGUI;
    private ORMAccess ormAccess;

    private GlobalData globalData;

    public ControleurXMLCreation(ControleurGeneral ctrGeneral, MainGUI mainGUI, ORMAccess ormAccess) {
        //this.ctrGeneral=ctrGeneral;
        ControleurXMLCreation.mainGUI = mainGUI;
        this.ormAccess = ormAccess;
    }

    public void createXML() {
        new Thread() {
            public void run() {
                mainGUI.setAcknoledgeMessage("Creation XML... WAIT");
                long currentTime = System.currentTimeMillis();
                try {
                    globalData = ormAccess.GET_GLOBAL_DATA();
                    try {
                        Document doc = new Document();
                        XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());

                        Element projections = new Element("projections");

                        for (Projection p : globalData.getProjections()) {
                            Element projection = new Element("projection");
                            {
                                Element date = new Element("date");
                                {
                                    Calendar dateheure = p.getDateHeure();
                                    Element jour = new Element("jour");
                                    jour.addContent(Integer.toString(dateheure.get(Calendar.DAY_OF_MONTH)));
                                    Element mois = new Element("mois");
                                    mois.addContent(Integer.toString(dateheure.get(Calendar.MONTH)));
                                    Element annee = new Element("annee");
                                    annee.addContent(Integer.toString(dateheure.get(Calendar.YEAR)));
                                    Element heure = new Element("heure");
                                    heure.addContent(Integer.toString(dateheure.get(Calendar.HOUR_OF_DAY)));
                                    Element minute = new Element("minute");
                                    minute.addContent(Integer.toString(dateheure.get(Calendar.MINUTE)));

                                    date.addContent(jour);
                                    date.addContent(mois);
                                    date.addContent(annee);
                                    date.addContent(heure);
                                    date.addContent(minute);
                                }

                                Element salle = new Element("salle");
                                salle.addContent(p.getSalle().getNo());
                                salle.setAttribute("taille", Integer.toString(p.getSalle().getTaille()));


                                Element film = new Element("film");
                                {
                                    Film f = p.getFilm();

                                    film.setAttribute("film_id", "F" + f.getId());

                                    Element titre = new Element("titre");
                                    titre.addContent(f.getTitre());
                                    Element synopsis = new Element("synopsis");
                                    synopsis.addContent(f.getSynopsis());
                                    Element duree = new Element("duree");
                                    duree.addContent(Integer.toString(f.getDuree()));

                                    Element critiques = new Element("critiques");
                                    for (Critique c : f.getCritiques()) {
                                        Element critique = new Element("critique");
                                        {
                                            Element texte = new Element("texte");
                                            texte.addContent(c.getTexte());
                                            Element note = new Element("note");
                                            note.addContent(Integer.toString(c.getNote()));

                                            critique.addContent(texte);
                                            critique.addContent(note);
                                        }
                                        critiques.addContent(critique);
                                    }

                                    Element genres = new Element("genres");
                                    for (Genre g : f.getGenres()) {
                                        Element genre = new Element("genre");
                                        genre.addContent(g.getLabel());

                                        genres.addContent(genre);
                                    }

                                    Element mots_cles = new Element("mots_cles");
                                    for (Motcle mc : f.getMotcles()) {
                                        Element mot_cle = new Element("mot_cle");
                                        mot_cle.addContent(mc.getLabel());

                                        mots_cles.addContent(mot_cle);
                                    }

                                    Element langages = new Element("langages");
                                    for (Langage l : f.getLangages()) {
                                        Element langage = new Element("langage");
                                        langage.addContent(l.getLabel());

                                        langages.addContent(langage);
                                    }

                                    Element photo = new Element("photo");
                                    photo.setAttribute("url", "" + f.getPhoto());


                                    Set<Acteur> acteurSet = new HashSet<>();

                                    for (RoleActeur ra : f.getRoles()) {
                                        acteurSet.add(ra.getActeur());
                                    }


                                    Element acteurs = new Element("acteurs");
                                    for (Acteur a : acteurSet) {
                                        Element acteur = new Element("acteur");

                                        {
                                            acteur.setAttribute("sexe", a.getSexe().name());
                                            Element nom = new Element("nom");
                                            nom.addContent(a.getNom());
                                            Element nom_naissance = new Element("nom_naissance");
                                            nom_naissance.addContent(a.getNomNaissance());
                                            Element biographie = new Element("biographie");
                                            biographie.addContent(a.getBiographie());

                                            Element roles = new Element("roles");
                                            {
                                                for(RoleActeur ra : f.getRoles()){
                                                    if(ra.getActeur().getId() == a.getId()){
                                                        Element role = new Element("role");
                                                        {
                                                            Element personnage = new Element("nom");
                                                            personnage.addContent(ra.getPersonnage());
                                                            Element place = new Element("place");
                                                            place.addContent(Long.toString(ra.getPlace()));

                                                            role.addContent(personnage);
                                                            role.addContent(place);
                                                        }
                                                        roles.addContent(role);
                                                    }
                                                }
                                            }

                                            Element date_naissance = new Element("date_naissance");
                                            {
                                                Calendar dateHeure = a.getDateNaissance();
                                                Element jour = new Element("jour");
                                                Element mois = new Element("mois");
                                                Element annee = new Element("annee");
                                                if (dateHeure != null) {
                                                    jour.addContent(Integer.toString(dateHeure.get(Calendar.DAY_OF_MONTH)));
                                                    mois.addContent(Integer.toString(dateHeure.get(Calendar.MONTH)));
                                                    annee.addContent(Integer.toString(dateHeure.get(Calendar.YEAR)));

                                                }
                                                date_naissance.addContent(jour);
                                                date_naissance.addContent(mois);
                                                date_naissance.addContent(annee);
                                            }

                                            Element date_deces = new Element("date_deces");
                                            {
                                                Calendar dateHeure = a.getDateDeces();
                                                Element jour = new Element("jour");
                                                Element mois = new Element("mois");
                                                Element annee = new Element("annee");
                                                if (dateHeure != null) {
                                                    jour.addContent("" + Integer.toString(dateHeure.get(Calendar.DAY_OF_MONTH)));
                                                    mois.addContent("" + Integer.toString(dateHeure.get(Calendar.MONTH)));
                                                    annee.addContent("" + Integer.toString(dateHeure.get(Calendar.YEAR)));
                                                }
                                                date_deces.addContent(jour);
                                                date_deces.addContent(mois);
                                                date_deces.addContent(annee);
                                            }

                                            acteur.addContent(nom);
                                            acteur.addContent(nom_naissance);
                                            acteur.addContent(biographie);
                                            acteur.addContent(roles);
                                            acteur.addContent(date_naissance);
                                            acteur.addContent(date_deces);
                                        }

                                        acteurs.addContent(acteur);
                                    }

                                    film.addContent(titre);
                                    film.addContent(synopsis);
                                    film.addContent(duree);
                                    film.addContent(critiques);
                                    film.addContent(genres);
                                    film.addContent(mots_cles);
                                    film.addContent(langages);
                                    film.addContent(photo);
                                    film.addContent(acteurs);

                                }
                                projection.addContent(date);
                                projection.addContent(salle);
                                projection.addContent(film);
                            }
                            projections.addContent(projection);
                        }

                        doc.addContent(projections);

                        DocType docType = new DocType("projections","Plex.dtd");
                        doc.setDocType(docType);
                        outp.output(doc, new FileOutputStream("plex.xml"));

                        mainGUI.setAcknoledgeMessage("Creation XML... FINISHED");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    mainGUI.setErrorMessage("Construction XML impossible", e.toString());
                }
            }
        }.start();
    }

    public void createXStreamXML() {
        new Thread() {
            public void run() {
                mainGUI.setAcknoledgeMessage("Creation XML... WAIT");
                long currentTime = System.currentTimeMillis();
                try {
                    globalData = ormAccess.GET_GLOBAL_DATA();
                    globalDataControle();
                } catch (Exception e) {
                    mainGUI.setErrorMessage("Construction XML impossible", e.toString());
                }

                XStream xstream = new XStream();
                writeToFile("global_data.xml", xstream, globalData);
                System.out.println("Done [" + displaySeconds(currentTime, System.currentTimeMillis()) + "]");
                mainGUI.setAcknoledgeMessage("XML cree en " + displaySeconds(currentTime, System.currentTimeMillis()));
            }
        }.start();
    }

    private static void writeToFile(String filename, XStream serializer, Object data) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            serializer.toXML(data, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final DecimalFormat doubleFormat = new DecimalFormat("#.#");

    private static final String displaySeconds(long start, long end) {
        long diff = Math.abs(end - start);
        double seconds = ((double) diff) / 1000.0;
        return doubleFormat.format(seconds) + " s";
    }

    private void globalDataControle() {
        for (Projection p : globalData.getProjections()) {
            System.out.println("******************************************");
            System.out.println(p.getFilm().getTitre());
            System.out.println(p.getSalle().getNo());
            System.out.println("Acteurs *********");
            for (RoleActeur role : p.getFilm().getRoles()) {
                System.out.println(role.getActeur().getNom());
            }
            System.out.println("Genres *********");
            for (Genre genre : p.getFilm().getGenres()) {
                System.out.println(genre.getLabel());
            }
            System.out.println("Mot-cles *********");
            for (Motcle motcle : p.getFilm().getMotcles()) {
                System.out.println(motcle.getLabel());
            }
            System.out.println("Langages *********");
            for (Langage langage : p.getFilm().getLangages()) {
                System.out.println(langage.getLabel());
            }
            System.out.println("Critiques *********");
            for (Critique critique : p.getFilm().getCritiques()) {
                System.out.println(critique.getNote());
                System.out.println(critique.getTexte());
            }
        }
    }
}



