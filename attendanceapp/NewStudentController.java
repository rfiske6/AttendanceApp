package attendanceapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static attendanceapp.AttendanceApp.primaryStage;
import static attendanceapp.AttendanceApp.scene1;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author fiske
 */
public class NewStudentController {

    /**
     * @param args the command line arguments
     */
    public static String xmlStudents = "";///home/fiske/students.xml";
    @FXML
    private Button btnCancelAdd;

    @FXML
    private Button btnConfirmAdd;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    void btnCancelAddClick(ActionEvent event) {
        primaryStage.setScene(scene1);
    }

    
    
    public static void createXML()
    {
        
      try {
         DocumentBuilderFactory dbFactory =
         DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.newDocument();
         
         // root element
         Element rootElement = doc.createElement("Students");
         doc.appendChild(rootElement);

     

         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(xmlStudents));
         transformer.transform(source, result);
         
         // Output to console for testing
         StreamResult consoleResult = new StreamResult(System.out);
         transformer.transform(source, consoleResult);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
    
    
    
    @FXML
    void btnConfirmAddClick(ActionEvent event) {

        File file = new File(xmlStudents);
try{
        if (file.exists() == false) {
               // file.createNewFile();
                createXML();
            
        }
        
         boolean found = false;
         File inputFile = new File(xmlStudents);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("Student");
         //String txt = nList.item(0).getNodeName();
         
         for (int i = 0; i < nList.getLength(); i++)
         {
             Element curNode = (Element)nList.item(i);
             if (curNode.getAttribute("ID").compareTo(txtID.getText()) == 0)
             {
                     System.out.println("found\n");
                     curNode.setAttribute("Code", txtCode.getText());
                     curNode.setAttribute("Name", txtName.getText());
                     found = true;
                     break;
             }
         }
         
         if (found == false)
         {
             Element newNode = doc.createElement("Student");
             newNode.setAttribute("Name", txtName.getText());
             newNode.setAttribute("ID", txtID.getText());
             newNode.setAttribute("Code", txtCode.getText());
             nList = doc.getElementsByTagName("Students");
             nList.item(0).appendChild(newNode);
             
         }
         
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(xmlStudents));
         transformer.transform(source, result);
         

         } catch (IOException ex) {
                Logger.getLogger(NewStudentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
            Logger.getLogger(NewStudentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(NewStudentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(NewStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        primaryStage.setScene(scene1);
    }

    public void clearNewStdDialog() {
        txtCode.clear();

        txtID.clear();

        txtName.clear();

    }

}
