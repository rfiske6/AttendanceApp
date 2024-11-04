/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import attendanceapp.NewStudentController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javafx.scene.image.Image;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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
public class Csv_attend_convert {
    //s23_students_601.xml
    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException
    {
        String inputFilename = "comma.csv";
        String outputFilename = "f23_students_260.xml";
        Scanner input = new Scanner(new File(inputFilename));
        
        
        
        
                 File inputFile = new File(outputFilename);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("Student");
         String txt = nList.item(0).getNodeName();
         
         
        
        
        
        
        
        while (input.hasNextLine())
        {
            String line = input.nextLine();
            
            
            
            String[] tokens = line.split(",");
            if (tokens.length < 10)
            {
                continue;
            }
            String id = tokens[9];
            
            String date = tokens[1];
            String time = tokens[2];

            if (date.length() < 4)
            {
                System.out.printf("Error for date: %s\n", id);
                continue;
            }
            
            LocalDate lDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
            LocalTime lTime = LocalTime.parse(time,DateTimeFormatter.ofPattern("hh:mm:ss a"));
            
            for (int i = 0; i < nList.getLength(); i++)
         {
             Element curNode = (Element)nList.item(i);
             if (curNode.getAttribute("ID").compareTo(id) == 0)
             {
                     System.out.println("found\n");
                     
                      
                     
                     Element newNode = doc.createElement("Attend");
                     newNode.setAttribute("Time",lTime.toString());
                     newNode.setAttribute("Date",lDate.toString());

                     curNode.appendChild(newNode);
                    
                     
                     break;
             }
         }
        
        }
        
        
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(outputFilename));
         transformer.transform(source, result);
         
        
    }
}
