/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import attendanceapp.NewStudentController;
import static attendanceapp.NewStudentController.xmlStudents;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.util.Pair;
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

/**
 *
 * @author fiske
 */
public class MergeFiles {
    
    void run() throws Exception
    {
           String file1, file2, file3;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter first file name");
        file1 = input.next();
        System.out.println("Enter second file name");
        file2 = input.next();
        System.out.println("Enter output file name");
        file3 = input.next();
        ArrayList<StudentRecord> students = new ArrayList<>();
        try
        {
        File inputFile = new File(file1);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("Student");
         String txt = nList.item(0).getNodeName();
         
         for (int i = 0; i < nList.getLength(); i++)
         {
             Element curNode = (Element)nList.item(i);
                     
             StudentRecord newStudent = new StudentRecord();
             students.add(newStudent);
             
             newStudent.name = curNode.getAttribute("Name");
             newStudent.id = curNode.getAttribute("ID");
             newStudent.code = curNode.getAttribute("Code");
             newStudent.image = curNode.getAttribute("Image");
             
             NodeList children = curNode.getChildNodes();
             
             for (int c = 0; c < children.getLength(); c++)
             {
                 Element curChild = (Element)children.item(c);
                 
                 if (curChild.getNodeName().compareTo("Attend") == 0)
                 {
                    String sdate = curChild.getAttribute("Date");
                    String stime = curChild.getAttribute("Time");
                    LocalDate lDate = LocalDate.parse(sdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalTime lTime = LocalTime.parse(stime);
                    newStudent.attend.add(new Pair(lDate, lTime));
                 }
             }

         }
         
    















         inputFile = new File(file2);
         dbFactory = DocumentBuilderFactory.newInstance();
         dBuilder = dbFactory.newDocumentBuilder();
         doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         nList = doc.getElementsByTagName("Student");
         txt = nList.item(0).getNodeName();
         
         for (int i = 0; i < nList.getLength(); i++)
         {
             Element curNode = (Element)nList.item(i);
                     
             StudentRecord newStudent = null;
             String id = curNode.getAttribute("ID");
             boolean found = false;
             for (StudentRecord curStudent : students)
             {
                 if (curStudent.id.compareTo(id) == 0)
                 {
                     found = true;
                     newStudent = curStudent;
                     break;
                 }
             }
             if (found == false)
             {
                 newStudent = new StudentRecord();
                              students.add(newStudent);
             
             newStudent.name = curNode.getAttribute("Name");
             newStudent.id = curNode.getAttribute("ID");
             newStudent.code = curNode.getAttribute("Code");
             newStudent.image = curNode.getAttribute("Image");
             

             }

             
             NodeList children = curNode.getChildNodes();
             for (int c = 0; c < children.getLength(); c++)
             {
                 Element curChild = (Element)children.item(c);
                 
                 if (curChild.getNodeName().compareTo("Attend") == 0)
                 {
                    String sdate = curChild.getAttribute("Date");
                    String stime = curChild.getAttribute("Time");
                    LocalDate lDate = LocalDate.parse(sdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalTime lTime = LocalTime.parse(stime);
                    found = false;
                    
                    for (Pair<LocalDate, LocalTime> curAtt : newStudent.attend)
                    {
                        if (curAtt.getKey().compareTo(lDate) == 0 &&
                                curAtt.getValue().compareTo(lTime) == 0)
                        {
                            found = true;
                            break;
                        }
                    }
                    if (found == false)
                    {
                        newStudent.attend.add(new Pair(lDate, lTime));
                    }
                 }
             }

         }














dbFactory =
         DocumentBuilderFactory.newInstance();
          dBuilder = dbFactory.newDocumentBuilder();
          doc = dBuilder.newDocument();
                    Element rootElement = doc.createElement("Students");
  doc.appendChild(rootElement);

                    nList = doc.getElementsByTagName("Students");
           
          for (StudentRecord curStudent : students)
          {
             Element newNode = doc.createElement("Student");
             newNode.setAttribute("Name", curStudent.name);
             newNode.setAttribute("ID", curStudent.id);
             newNode.setAttribute("Code", curStudent.code);
             nList.item(0).appendChild(newNode);
             for (Pair<LocalDate, LocalTime> curPair : curStudent.attend)
             {
                 Element attNode = doc.createElement("Attend");
                     attNode.setAttribute("Time",curPair.getValue().format(DateTimeFormatter.ISO_TIME));
                     attNode.setAttribute("Date",curPair.getKey().format(DateTimeFormatter.ISO_DATE));

                     newNode.appendChild(attNode);
                    
             }
          }

TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(file3));
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
    
    public static void main(String[] args) throws Exception
    {
        
        MergeFiles tmp = new MergeFiles();
        tmp.run();
    }
    
    class StudentRecord
    {
        public String name, id, code, image;
        ArrayList<Pair<LocalDate, LocalTime>> attend = new ArrayList<>();
    

        @Override
        public boolean equals(Object other)
        {
            if ((other instanceof StudentRecord) == false)
            {
                return(false);
            }
            StudentRecord sother = (StudentRecord)other;
            return(sother.id.compareTo(id) == 0);
        }
    }
}
