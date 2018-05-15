import jdk.internal.org.xml.sax.SAXException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

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

    




    public static void main(String[] args) {
        String xmlFile = "plex.xml";
        Document document = getDOMParsedDocument(xmlFile);

        Element rootNode = document.getRootElement();
        System.out.println("Root Element :: " + rootNode.getName());

    }
}
