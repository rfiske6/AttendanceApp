package attendanceapp;
//26439AttendanceApp
//20656
//31052
//1722
//15145

import java.io.File;
import java.io.FileInputStream;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AttendanceApp extends Application implements Initializable {

    
    static
    {
        System.out.println("test");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
    static Stage primaryStage;
    static Scene scene1;
    static Scene scene2;
    static Scene scene3;
    static NewStudentController studentController;
    //static HashSet<LocalDate> days = new HashSet<>();
        
     @Override
    public void start(Stage stage) throws IOException {
  
        try
        {
            primaryStage = stage;
            Parent root = null, studentDialog = null, reportDialog = null;
            
            
            
                       DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;
            NodeList nList;
            String txt;
            
            
            
            
            
            /*
            File daysFile = new File(NewStudentController.xmlDays);
            
            try
            {
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(daysFile);
                doc.getDocumentElement().normalize();
                nList = doc.getElementsByTagName("Day");
                txt = nList.item(0).getNodeName();
                
                
                for (int i = 0; i < nList.getLength(); i++)
                {
                    Element curNode = (Element)nList.item(i);
                    LocalDate date = LocalDate.parse(curNode.getAttribute("Date"));
                    days.add(date);
                    
                }
                
            }
            catch(Exception e)
            {
                
            }
            */
            
            FXMLLoader mainLoader = new FXMLLoader(
                    getClass().getResource("MainMenu.fxml"));
            
            
            FXMLLoader newStudentLoader = new FXMLLoader(
                    getClass().getResource("NewStudentDialog.fxml"));
            
            FXMLLoader reportLoader = new FXMLLoader(
                    getClass().getResource("ReportDialog.fxml"));
            
            root = mainLoader.load();
            studentDialog = newStudentLoader.load();
            reportDialog = reportLoader.load();
            
            
            studentController =
                    newStudentLoader.<NewStudentController>getController();
            
            
            scene1 = new Scene(root, 800, 800);
            scene2 = new Scene(studentDialog, 800, 800);
            scene3 = new Scene(reportDialog, 800, 800);
            
            stage.setMaximized(true);
            stage.setScene(scene1);
            stage.show();
            
            
            
            
            
        }
        catch(Exception ex)
        {
            Logger.getLogger(AttendanceApp.class.getName()).log(Level.SEVERE, null, ex);
            
        }      
    }
    
    
        @FXML
    private Button btnNewStudent;

    @FXML
    void clkNewStudent(ActionEvent event) {
        studentController.clearNewStdDialog();
        primaryStage.setScene(scene2);
               

        
    }
    
    @FXML
    void clkClass(ActionEvent event) {
        ClassData sel = lvClasses.getSelectionModel().getSelectedItem();
        NewStudentController.xmlStudents = sel.file;
        btnClass.setDisable(true);
        
    }
    
        @FXML
    void enableClass(MouseEvent event) {
        btnClass.setDisable(false);
        lblInput.requestFocus();
    }
    
     @FXML
    void clkReport(ActionEvent event) {
               

          primaryStage.setScene(scene3);
    }
    
    
    @FXML
    private Button btnReport;
    
    @FXML
    private Button btnClass;
    
    @FXML
    private Label lblInput;
    
        @FXML
    private Label lblID;

    @FXML
    private ImageView ivImage;
    
    @FXML
    private Label lblName;
    
    @FXML
    private ListView<ClassData> lvClasses;


    
    StringBuffer lblBuffer = new StringBuffer();
    
        @FXML
    void keyTyped(javafx.scene.input.KeyEvent event) {
        String pressed = event.getText();
        boolean hasNewline = event.getCode().compareTo(KeyCode.ENTER) == 0;
        lblBuffer.append(pressed);  
        if (hasNewline)
        {
          lblBuffer.setLength(lblBuffer.length()-1);
          lblInput.setText(lblBuffer.toString());
          lblBuffer.setLength(0);
          
         
          
          
          
          File file = new File(NewStudentController.xmlStudents);
try{
        if (file.exists() == false) {
               // file.createNewFile();
                NewStudentController.createXML();
            
        }
        
         boolean found = false;
         File inputFile = new File(NewStudentController.xmlStudents);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("Student");
         String txt = nList.item(0).getNodeName();
         
        LocalDate now = LocalDate.now();
        /*
        if (!days.contains(now))
        {
             try
        {
            File xdaysFile = new File(NewStudentController.xmlDays);
        
        DocumentBuilderFactory xdbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder xdBuilder = xdbFactory.newDocumentBuilder();
         Document xdoc = xdBuilder.parse(xdaysFile);
         xdoc.getDocumentElement().normalize();
         var curNode = xdoc.getDocumentElement();
                             Element newNode = xdoc.createElement("Day");
                     newNode.setAttribute("Date",LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
                    days.add(LocalDate.now());
                     curNode.appendChild(newNode);
          */           
                     
                     
         /*
         TransformerFactory xtransformerFactory = TransformerFactory.newInstance();
         Transformer xtransformer = xtransformerFactory.newTransformer();
         DOMSource xsource = new DOMSource(xdoc);
         StreamResult xresult = new StreamResult(new File(NewStudentController.xmlDays));
         xtransformer.transform(xsource, xresult);
         
                
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        }
        */ 
         while (lblInput.getText().length() < 5)
         {
             lblInput.setText("0" + lblInput.getText());
         }
         for (int i = 0; i < nList.getLength(); i++)
         {
             Element curNode = (Element)nList.item(i);
             if ((curNode.getAttribute("Code").compareTo(lblInput.getText()) == 0) ||
                     curNode.getAttribute("ID").compareTo(lblInput.getText()) == 0)
             {
                     System.out.println("found\n");
                     
                      
                     
                     Element newNode = doc.createElement("Attend");
                     newNode.setAttribute("Time",LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
                     newNode.setAttribute("Date",LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));

                     curNode.appendChild(newNode);
                    
                     lblName.setText(curNode.getAttribute("Name"));
                     lblID.setText(curNode.getAttribute("ID"));
                     String image = curNode.getAttribute("Image");
                     if (image != null && image.length() > 0)
                     {
                         try
                         {
                        FileInputStream st = new FileInputStream(image);
                        ivImage.setImage(new Image(st));
                         }
                         catch(Exception e)
                         {
                             System.out.println("Image not found");
                         }
                     }
                     found = true;
                     break;
             }
         }
         
         if (found == false)
         {
             ivImage.setImage(null);
             lblName.setText("NOT FOUND");
             lblID.setText("NOT FOUND");
         }
         
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(NewStudentController.xmlStudents));
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
        
          
          
          
        }
        
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
           
        
        try {
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;
            NodeList nList;
            String txt;
            
            File cfile = new File("classes.xml");
            
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(cfile);
            doc.getDocumentElement().normalize();
            nList = doc.getElementsByTagName("class");
            txt = nList.item(0).getNodeName();
            
            for (int i = 0; i < nList.getLength(); i++)
            {
                Element curNode = (Element)nList.item(i);
                ClassData newData = new ClassData();
                newData.name = curNode.getAttribute("name");
                newData.file = curNode.getAttribute("file");
                
                lvClasses.getItems().add(newData);
            }
            
            
        
            btnNewStudent.setFocusTraversable(false);
            btnClass.setFocusTraversable(false);
            lvClasses.setFocusTraversable(false);
            lblInput.setFocusTraversable(true);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AttendanceApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(AttendanceApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AttendanceApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
   public class ClassData
   {
       public String file;
       public String name;
       
       public String toString()
       {
           return(name);
       }
   }

}

