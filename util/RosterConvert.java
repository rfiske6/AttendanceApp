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
public class RosterConvert {

    //s23_students_601.xml
    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        String inputFilename = "/home/fiske/Downloads/classroster_CIS_340_50.csv";
        String outputFilename = "f23_students_cis_340.xml";
        String folder = "images";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("Students");
        document.appendChild(root);
        
        Scanner input = new Scanner(new File(inputFilename)); 

        while (input.hasNextLine()) {
            String line = input.nextLine();

            String[] tokens = line.split(",");
            if (tokens.length < 5) {
                continue;
            }

            Element student = document.createElement("Student");
            root.appendChild(student);

            String id = tokens[0];
            String lname = tokens[1];
            String fname = tokens[2];
            String code = tokens[3];
            String email = tokens[4];
            
            while (id.length() < 5)
            {
                id = "0" + id;
            }
            student.setAttribute("Name", lname + ", " + fname);
            student.setAttribute("ID", id);
            student.setAttribute("Code", code);
            student.setAttribute("Email", email);
            student.setAttribute("Image", folder + "/" + id + ".png");
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(outputFilename));

        transformer.transform(source, result);

    }
}
