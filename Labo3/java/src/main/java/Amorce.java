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


            /**
             * TODO remplir document de plein plein de xml
             */





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
