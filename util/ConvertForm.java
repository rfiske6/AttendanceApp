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
import java.util.HashMap;
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
public class ConvertForm {

    //s23_students_601.xml
    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        //String inputFilename = "/home/fiske/Downloads/classroster_CIS_434.csv";
        String outputFilename = "create_f24_students_cis_480.xml";
        String folder = "images";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("Students");
        document.appendChild(root);

        File[] files = new File("files").listFiles();
        HashMap<String, String> imageNames = new HashMap<>();
 
        for (File curFile : files)
        {
        
            if (curFile.getName().toLowerCase().contains("rosterdata") == false || curFile.getName().endsWith(".docx"))
            {
                continue;
            }
        Scanner input = new Scanner(curFile); 

           /*
            String id = tokens[0];
            String lname = tokens[1];
            String fname = tokens[2];
            String code = tokens[3];
            String email = tokens[4];
            */
            String id,name,code,email, image;
            id = name = code = email = image = "_";
        while (input.hasNextLine()) {
            String line = input.nextLine();
            try
            {
            String[] parts = line.split(":");
            parts[0] = parts[0].trim();
            parts[1] = parts[1].trim();
            if (parts[0].equalsIgnoreCase("Name"))
            {
                name = parts[1];
            }
            else if (parts[0].equalsIgnoreCase("StudentID"))
            {
                id = parts[1];
            } 
            else if (parts[0].equalsIgnoreCase("IDCode"))
            {
                code = parts[1];
            } 
            else if (parts[0].equalsIgnoreCase("Email"))
            {
                email = parts[1];
            } 
            else if (parts[0].equalsIgnoreCase("ImageFileName"))
            {
                    imageNames.put(id, parts[1]);
                    image = id + "_" + parts[1];
            }
            }
            catch(Exception e)
            {
                System.err.println("Line error: " + line);
            }
        }
        if (image.equalsIgnoreCase("_"))
        {
            image = id + ".png";
        }   
            Element student = document.createElement("Student");
            root.appendChild(student);

            while (id.length() < 5)
            {
                id = "0" + id;
            }
            student.setAttribute("Name", name);
            student.setAttribute("ID", id);
            student.setAttribute("Code", code);
            student.setAttribute("Email", email);
            student.setAttribute("Image", folder + "/" + image);
        
        input.close();
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(outputFilename));

        transformer.transform(source, result);

        for (File curFile : files)
        {
            if (curFile.getName().endsWith(".txt"))
            {
                System.out.println("skip: " + curFile.getName());
                continue;
            }
            String[] parts = curFile.getName().split("_");
            String id = parts[1];
            String filename = imageNames.get(id);
            
            if (filename == null)
            {
                System.out.println("No image for: " + id);
                continue;
            }
            if (curFile.getName().contains(filename))
            {
                String src = "files/" + curFile.getName();
                String dst = "images/" + id + "_" + filename;
                Process p = Runtime.getRuntime().exec(new String[] {"cp", src, dst});
                System.out.println("cp " + src + " " + dst);
            }
        }
    }
        
}
