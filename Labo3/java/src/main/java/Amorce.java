import jdk.internal.org.xml.sax.SAXException;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.ProcessingInstruction;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Amorce {

    /**
     * permet de lire un fichier xml et de le transformer en document
     *
     * @param fileName
     * @return
     */
    private static Document getDOMParsedDocument(final String fileName)
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //If want to make namespace aware.
            //factory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = documentBuilder.parse(fileName);
            document = new DOMBuilder().build(w3cDocument);
        }
        catch (IOException | org.xml.sax.SAXException | ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * doit créer un fichier qui se base sur la dtd donné par le prof
     *
     * On prends le xml du labo2, on voyage dedans pour prendre les informations qu'il nous faut
     *
     * afin de faire un nouveau xml désiré
     *
     * @param labo2
     */
    private static void createXML(Document labo2){

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

            Element plex = new Element("plex");

            Element projections = new Element("projections");

            for(Element projectionTMP : labo2.getRootElement().getChildren("projection")){
                Element projection = new Element("projection");

                Element salle = new Element("salle");
                salle.setAttribute("taille", projectionTMP.getChild("salle").getAttribute("taille").getValue());
                salle.addContent(projectionTMP.getChild("salle").getContent(0).getValue());

                projection.addContent(salle);

                Element date_heure = new Element("date_heure");
                Element date = projectionTMP.getChild("date");

                String date_string = "";
                date_string += String.format("%02d", Integer.parseInt(date.getChild("jour").getValue()))+ ".";
                date_string += String.format("%02d", Integer.parseInt(date.getChild("mois").getValue())) + ".";
                date_string += String.format("%04d", Integer.parseInt(date.getChild("annee").getValue())) + " ";
                date_string += String.format("%02d", Integer.parseInt(date.getChild("heure").getValue()))+ ":";
                date_string += String.format("%02d", Integer.parseInt(date.getChild("minute").getValue()));
                date_heure.setAttribute("format", "dd.MM.YYYY HH:mm");
                date_heure.addContent(date_string);

                projection.addContent(date_heure);

                projection.setAttribute("film_id", projectionTMP.getChild("film").getAttributeValue("film_id"));
                projection.setAttribute("titre", projectionTMP.getChild("film").getChild("titre").getValue());


                projections.addContent(projection);
            }

            plex.addContent(projections);

            Element films = new Element("films");

            for(Element projectionTMP : labo2.getRootElement().getChildren("projection")){
                Element film = new Element("film");
                film.setAttribute("no", projectionTMP.getChild("film").getAttributeValue("film_id"));

                Element filmTMP = projectionTMP.getChild("film");

                Element titre = new Element("titre");
                titre.addContent(filmTMP.getChild("titre").getValue());

                Element duree = new Element("duree");
                duree.setAttribute("format","minutes");
                duree.addContent(filmTMP.getChild("duree").getValue());

                Element synopsys = new Element("synopsys");
                synopsys.addContent(filmTMP.getChild("synopsis").getValue());

                film.addContent(titre);
                film.addContent(duree);
                film.addContent(synopsys);

                if(!filmTMP.getChild("photo").getAttribute("url").equals("null")){
                    Element photo = new Element("photo");
                    photo.setAttribute("url", filmTMP.getChild("photo").getAttribute("url").getValue());
                    film.addContent(photo);
                }

                Element critiques = new Element("critiques");

                for(Element critique_TMP : filmTMP.getChild("critiques").getChildren("critique")){
                    Element critique = new Element("critique");

                    critique.setAttribute("note", critique_TMP.getChild("note").getValue());

                    critique.addContent(critique_TMP.getChild("texte").getValue());


                    critiques.addContent(critique);
                }
                film.addContent(critiques);

                Element langages = new Element("langages");

                String langages_string = "";
                for(Element langage : filmTMP.getChild("langages").getChildren("langage")){

                    langages_string += langage.getValue() + " ";
                }

                langages.setAttribute("liste", langages_string);

                film.addContent(langages);


                films.addContent(film);
            }
            plex.addContent(films);

            /**
             * TODO remplir document de plein plein de xml
             */


            document.addContent(plex);

            outp.output(document, new FileOutputStream("projections.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String xmlFile = "plex.xml";
        Document labo2 = getDOMParsedDocument(xmlFile);

        Element rootNode = labo2.getRootElement();
        System.out.println("Root Element :: " + rootNode.getName());

        createXML(labo2);
    }
}
