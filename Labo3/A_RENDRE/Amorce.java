import jdk.internal.org.xml.sax.XMLReader;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.ProcessingInstruction;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Amorce {

    /**
     * @param date : date à parser
     * @return la date dans le format "dd.MM.YYYY HH:mm"
     */
    private static String getDateHour(Element date) {
        String date_string = getDate(date) + " ";
        date_string += String.format("%02d", Integer.parseInt(date.getChild("heure").getValue())) + ":";
        date_string += String.format("%02d", Integer.parseInt(date.getChild("minute").getValue()));

        return date_string;
    }


    /**
     * @param date : date à parser
     * @return la date dans le format "dd.MM.YYYY
     */
    private static String getDate(Element date) {
        String date_string = "";
        date_string += String.format("%02d", Integer.parseInt(date.getChild("jour").getValue())) + ".";
        date_string += String.format("%02d", Integer.parseInt(date.getChild("mois").getValue())) + ".";
        date_string += String.format("%04d", Integer.parseInt(date.getChild("annee").getValue()));

        return date_string;
    }

    /**
     * permet de lire un fichier xml et de le transformer en Document
     *
     * @param fileName
     * @return
     */
    private static Document getDOMParsedDocument(final String fileName) {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //If want to make namespace aware.
            //factory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = documentBuilder.parse(fileName);
            document = new DOMBuilder().build(w3cDocument);
        } catch (IOException | org.xml.sax.SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * doit créer un fichier qui se base sur la dtd donné par le prof
     * <p>
     * On prends le xml du labo2, on voyage dedans pour prendre les informations qu'il nous faut
     * <p>
     * afin de faire un nouveau xml désiré
     *
     * @param labo2
     */
    private static void createXML(Document labo2) {

        try {

            Document document = new Document();
            XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());

            // -- Ajout d'une référence à la DTD
            DocType docType = new DocType("plex", "projections.dtd");
            document.addContent(docType);

            // -- Ajout de la référence à la feuille XSL
            ProcessingInstruction piXSL = new ProcessingInstruction("xml-stylesheet");
            HashMap<String, String> piAttributes = new HashMap<String, String>();
            piAttributes.put("type", "text/xsl");
            piAttributes.put("href", "projections.xsl");
            piXSL.setData(piAttributes);
            document.addContent(piXSL);

            //Element racine
            Element plex = new Element("plex");

            //----------------------------------------------------------------------------------------------------------
            //                                        PROJECTIONS
            //----------------------------------------------------------------------------------------------------------
            Element projections = new Element("projections");

            for (Element projectionTMP : labo2.getRootElement().getChildren("projection")) {
                Element projection = new Element("projection");

                Element salle = new Element("salle");
                salle.setAttribute("taille", projectionTMP.getChild("salle").getAttribute("taille").getValue());
                salle.addContent(projectionTMP.getChild("salle").getContent(0).getValue());

                projection.addContent(salle);

                Element date_heure = new Element("date_heure");
                Element date = projectionTMP.getChild("date");

                date_heure.setAttribute("format", "dd.MM.YYYY HH:mm");
                date_heure.addContent(getDateHour(date));

                projection.addContent(date_heure);

                projection.setAttribute("film_id", projectionTMP.getChild("film").getAttributeValue("film_id"));
                projection.setAttribute("titre", projectionTMP.getChild("film").getChild("titre").getValue());


                projections.addContent(projection);
            }

            plex.addContent(projections);


            //----------------------------------------------------------------------------------------------------------
            //                                        FILMS
            //----------------------------------------------------------------------------------------------------------

            Element films = new Element("films");

            for (Element projectionTMP : labo2.getRootElement().getChildren("projection")) {
                Element film = new Element("film");
                film.setAttribute("no", projectionTMP.getChild("film").getAttributeValue("film_id"));

                Element filmTMP = projectionTMP.getChild("film");

                Element titre = new Element("titre");
                titre.addContent(filmTMP.getChild("titre").getValue());

                Element duree = new Element("duree");
                duree.setAttribute("format", "minutes");
                duree.addContent(filmTMP.getChild("duree").getValue());

                Element synopsys = new Element("synopsys");
                synopsys.addContent(filmTMP.getChild("synopsis").getValue());

                film.addContent(titre);
                film.addContent(duree);
                film.addContent(synopsys);

                if (!filmTMP.getChild("photo").getAttribute("url").equals("null")) {
                    Element photo = new Element("photo");
                    photo.setAttribute("url", filmTMP.getChild("photo").getAttribute("url").getValue());
                    film.addContent(photo);
                }

                Element critiques = new Element("critiques");

                for (Element critique_TMP : filmTMP.getChild("critiques").getChildren("critique")) {
                    Element critique = new Element("critique");

                    critique.setAttribute("note", critique_TMP.getChild("note").getValue());

                    critique.addContent(critique_TMP.getChild("texte").getValue());


                    critiques.addContent(critique);
                }
                film.addContent(critiques);

                Element langages = new Element("langages");

                String langages_string = "";
                for (Element langage : filmTMP.getChild("langages").getChildren("langage")) {

                    langages_string += langage.getAttributeValue("no") + " ";
                }

                langages.setAttribute("liste", langages_string);

                film.addContent(langages);

                Element genres = new Element("genres");

                String genres_string = "";
                for (Element genre : filmTMP.getChild("genres").getChildren("genre")) {

                    genres_string += genre.getAttributeValue("no") + " ";
                }

                genres.setAttribute("liste", genres_string);

                film.addContent(genres);

                Element mots_cles = new Element("mots_cles");

                String mots_cles_string = "";
                for (Element mot_cle : filmTMP.getChild("mots_cles").getChildren("mot_cle")) {

                    mots_cles_string += mot_cle.getAttributeValue("no") + " ";
                }

                mots_cles.setAttribute("liste", mots_cles_string);

                film.addContent(mots_cles);

                Element roles = new Element("roles");

                for (Element acteurTMP : filmTMP.getChild("acteurs").getChildren("acteur")) {
                    for (Element roleTMP : acteurTMP.getChild("roles").getChildren("role")) {
                        Element role = new Element("role");
                        role.setAttribute("place", roleTMP.getChild("place").getValue());
                        role.setAttribute("personnage", roleTMP.getChild("nom").getValue());
                        role.setAttribute("acteur_id", acteurTMP.getAttributeValue("no"));
                        roles.addContent(role);
                    }
                }
                film.addContent(roles);

                films.addContent(film);
            }
            plex.addContent(films);


            //----------------------------------------------------------------------------------------------------------
            //                                        ACTEURS
            //----------------------------------------------------------------------------------------------------------

            Element acteurs = new Element("acteurs");

            ArrayList<String> alreadyPlaced = new ArrayList<>();

            for (Element projectionTMP : labo2.getRootElement().getChildren("projection")) {
                for (Element acteurTMP : projectionTMP.getChild("film").getChild("acteurs").getChildren("acteur")) {
                    if (!alreadyPlaced.contains(acteurTMP.getAttributeValue("no"))) {
                        Element acteur = new Element("acteur");
                        acteur.setAttribute("no", acteurTMP.getAttributeValue("no"));

                        alreadyPlaced.add(acteurTMP.getAttributeValue("no"));

                        Element nom = new Element("nom");
                        nom.addContent(acteurTMP.getChild("nom").getValue());

                        Element nom_naissance = new Element("nom_naissance");
                        nom_naissance.addContent(acteurTMP.getChild("nom_naissance").getValue());

                        Element sexe = new Element("sexe");
                        sexe.setAttribute("valeur", acteurTMP.getAttributeValue("sexe"));

                        Element date_naissance = new Element("date_naissance");
                        date_naissance.setAttribute("format", "dd.mm.yyyy");
                        if (!acteurTMP.getChild("date_naissance").getChild("jour").getValue().equals(""))
                            date_naissance.addContent(getDate(acteurTMP.getChild("date_naissance")));

                        Element date_deces = new Element("date_deces");
                        date_deces.setAttribute("format", "dd.mm.yyyy");
                        if (!acteurTMP.getChild("date_deces").getChild("jour").getValue().equals(""))
                            date_deces.addContent(getDate(acteurTMP.getChild("date_deces")));

                        Element biographie = new Element("biographie");
                        biographie.addContent(acteurTMP.getChild("biographie").getValue());

                        acteur.addContent(nom);
                        acteur.addContent(nom_naissance);
                        acteur.addContent(sexe);
                        acteur.addContent(date_naissance);
                        acteur.addContent(date_deces);
                        acteur.addContent(biographie);

                        acteurs.addContent(acteur);
                    }
                }
            }

            plex.addContent(acteurs);


            //----------------------------------------------------------------------------------------------------------
            //                                        LISTE_LANGAGES
            //----------------------------------------------------------------------------------------------------------

            Element liste_langages = new Element("liste_langages");

            SAXBuilder builder = new SAXBuilder();
            Document xmlDoc = builder.build(new File("plex.xml"));

            XPathFactory xpfac = XPathFactory.instance();
            XPathExpression xp = xpfac.compile("//film/langages/langage", Filters.element());

            List<Element> langages = (List<Element>) (xp.evaluate(xmlDoc));
            for (Element l : langages) {
                Element langage = new Element("langage");
                langage.addContent(l.getValue());
                langage.setAttribute("no", l.getAttributeValue("no"));


                liste_langages.addContent(langage);
            }

            plex.addContent(liste_langages);


            //----------------------------------------------------------------------------------------------------------
            //                                        LISTE_GENRES
            //----------------------------------------------------------------------------------------------------------

            Element liste_genres = new Element("liste_genres");

            for (Element projectionTMP : labo2.getRootElement().getChildren("projection")) {
                for (Element genreTMP : projectionTMP.getChild("film").getChild("genres").getChildren("genre")) {
                    Element genre = new Element("genre");
                    genre.addContent(genreTMP.getValue());
                    genre.setAttribute("no", genreTMP.getAttributeValue("no"));


                    liste_genres.addContent(genre);
                }
            }
            plex.addContent(liste_genres);


            //----------------------------------------------------------------------------------------------------------
            //                                        LISTE_MOTS_CLES
            //----------------------------------------------------------------------------------------------------------

            Element liste_mots_cles = new Element("liste_mots_cles");

            for (Element projectionTMP : labo2.getRootElement().getChildren("projection")) {
                for (Element genreTMP : projectionTMP.getChild("film").getChild("mots_cles").getChildren("mot_cle")) {
                    Element mot_cle = new Element("mot_cle");
                    mot_cle.addContent(genreTMP.getValue());
                    mot_cle.setAttribute("no", genreTMP.getAttributeValue("no"));


                    liste_mots_cles.addContent(mot_cle);
                }
            }
            plex.addContent(liste_mots_cles);

            document.addContent(plex);


            //we create the .xml file
            outp.output(document, new FileOutputStream("projections.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String xmlFile = "plex.xml";
        Document labo2 = getDOMParsedDocument(xmlFile);

        createXML(labo2);
    }
}