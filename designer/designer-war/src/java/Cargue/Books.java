package Cargue;

import java.io.BufferedReader;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@ManagedBean
@RequestScoped
public class Books {

    public ArrayList<Book> getBooks() {

        ArrayList al = new ArrayList();
        Book b;

        // read data from XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // get physical path for BOOKS.XML. First get access to ServletContext using FacesContext
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            String filepath = context.getRealPath("WEB-INF/classes/Cargue/books.xml");  // get physical path for /books.xml

            document = builder.parse(new File(filepath));
            Element root = document.getDocumentElement();

            NodeList books = root.getChildNodes();

            for (int i = 0; i < books.getLength(); i++) {
                // skip the rest if node is not an elemtn
                if (books.item(i).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                NodeList bookdetails = books.item(i).getChildNodes();

                // create a book with the data from book element in XML document
                b = new Book(bookdetails.item(1).getTextContent(),
                        bookdetails.item(3).getTextContent(),
                        Integer.parseInt(bookdetails.item(5).getTextContent()));
                al.add(b);  // add book to ArrayList
            }
            return al;
        } catch (Exception e) {
            System.out.println("\n** Parsing error" + ", line " + e.getMessage());
            return null;
        }
    }

    public void leerTxt() throws FileNotFoundException, IOException {
        try {
            File archivo = new File("C:\\Prueba\\Archivos_Planos_Cargados\\books.prn");
            FileReader fr = new FileReader(archivo);
            BufferedReader bf = new BufferedReader(fr);
            String sCadena;


            while ((sCadena = bf.readLine())!= null) {
                String titulo = sCadena.substring(0, 45).trim();
                String author = sCadena.substring(46, 72).trim();
                String price = sCadena.substring(73).trim();
                System.out.println(titulo + "_");
                System.out.println(author + "_");
                System.out.println(price + "_");
            }
        } catch (Exception e) {
            System.out.println("Excepcion: " + e);
        }

    }
}
