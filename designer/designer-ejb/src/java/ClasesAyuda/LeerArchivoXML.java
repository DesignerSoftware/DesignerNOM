/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author user
 */
public class LeerArchivoXML {

   public List<CadenasDesignerInn> leerArchivoDesignerInn() {
      try {
         InputStream fXmlFile = getClass().getResourceAsStream("../archivosConfiguracion/cadenasDesignerInn.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(fXmlFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("cadenaDesignerInn");
         List<CadenasDesignerInn> listaCadenas = new ArrayList<CadenasDesignerInn>();

         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               listaCadenas.add(new CadenasDesignerInn(eElement.getAttribute("id"),
                       eElement.getElementsByTagName("nombrepoolJPA").item(0).getTextContent(),
                       eElement.getElementsByTagName("usapool").item(0).getTextContent(),
                       eElement.getElementsByTagName("bd").item(0).getTextContent()));
            }
         }
         Collections.sort(listaCadenas);
         return listaCadenas;
      } catch (ParserConfigurationException e) {
         System.out.println("Error LeerArchivoXML.leerArchivoDesignerInn");
         System.out.println("Error parseando el archivo. " + e);
         return null;
      } catch (SAXException e) {
         System.out.println("Error LeerArchivoXML.leerArchivoDesignerInn");
         System.out.println("Error SAX. " + e);
         return null;
      } catch (IOException e) {
         System.out.println("Error LeerArchivoXML.leerArchivoDesignerInn");
         System.out.println("Error leyendo el archivo. " + e);
         return null;
      } catch (DOMException e) {
         System.out.println("Error LeerArchivoXML.leerArchivoDesignerInn: ");
         System.out.println("Error en DOM. " + e);
         return null;
      }
   }

}
