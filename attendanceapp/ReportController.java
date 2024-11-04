package attendanceapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReportController {

    @FXML
    private TableView<StudentAttend> tblData;

    @FXML
    private ImageView ivStudent;

    @FXML
    private Button btnCancelAdd;

    @FXML
    private Button btnLoad;

    @FXML
    void btnCancelAddClick(ActionEvent event) {
        AttendanceApp.primaryStage.setScene(AttendanceApp.scene1);
    }

    @FXML
    void btnLoadClick(ActionEvent event) {
        try {

            ObservableList<StudentAttend> students
                    = FXCollections.observableArrayList();
            String file = NewStudentController.xmlStudents;
            HashMap<LocalDate, ArrayList<StudentAttend>> data = new HashMap<>();
            ArrayList<LocalDate> dates = new ArrayList<>();

            /* TableColumn<StudentAttend, String> nameCol = new TableColumn("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
           TableColumn<StudentAttend, String> idCol = new TableColumn("ID");
           idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
             */
            //tblData.getColumns().add(new TableColumn("Name"));
            //tblData.getColumns().add(new TableColumn("ID"));
            File inputFile = new File(NewStudentController.xmlStudents);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Student");
            String txt = nList.item(0).getNodeName();

            for (int i = 0; i < nList.getLength(); i++) {
                Element curNode = (Element) nList.item(i);

                String name = curNode.getAttribute("Name");
                String id = curNode.getAttribute("ID");
                String filename = curNode.getAttribute("Image");
                StudentAttend newStudent = new StudentAttend();
                newStudent.name = name;
                newStudent.id = id;
                newStudent.fileName = filename;
                System.out.printf("Name: %s ID: %s\n", name, id);
                NodeList children = curNode.getElementsByTagName("Attend");
                for (int j = 0; j < children.getLength(); j++) {
                    var node = children.item(j);
                    if (node instanceof Element) {
                        Element x = (Element) children.item(j);
                        //System.out.printf("\t%s\n", x.getAttribute("Date"));
                        String tmp = x.getAttribute("Date");
                        LocalDate date = LocalDate.parse(tmp);
                        if (dates.contains(date) == false) {
                            //    tblData.getColumns().add(new TableColumn(date.toString()));
                            dates.add(date);
                        }

                        newStudent.attend.put(date, LocalTime.parse(x.getAttribute("Time")));

                    }

                }
                students.add(newStudent);
            }
            //xml done
            /*for(StudentAttend curStudent : students)
            {
                tblData.getItems().add(curStudent);
            }*/

            tblData.setEditable(true);

            TableColumn colName = new TableColumn("Name");
            colName.setCellValueFactory(new PropertyValueFactory<StudentAttend, String>("name"));

            TableColumn colID = new TableColumn("ID");
            colID.setCellValueFactory(new PropertyValueFactory<StudentAttend, String>("id"));

            tblData.getColumns().addAll(colName, colID);

            for (LocalDate curDate : dates) {
                TableColumn<StudentAttend, String> classDate = new TableColumn();
                classDate.setText(curDate.toString());

                classDate.setCellValueFactory(new Callback<CellDataFeatures<StudentAttend, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<StudentAttend, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row

                        return new ReadOnlyStringWrapper(p.getValue().getAttend(curDate));
                    }
                });
                tblData.getColumns().add(classDate);
            }

            TableColumn<StudentAttend, String> attendPer = new TableColumn();
            attendPer.setText("Attendance Percentage");

            attendPer.setCellValueFactory(new Callback<CellDataFeatures<StudentAttend, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<StudentAttend, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row

                    return new ReadOnlyStringWrapper(p.getValue().getAttendPercent(dates.size()));
                }
            });
            tblData.getColumns().add(attendPer);

            tblData.setItems(students);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tblData.getSelectionModel().selectedItemProperty().addListener((item, oldSel, newSel) -> {
            if (newSel != null) {
                if (newSel.fileName != null && newSel.fileName.length() > 0) {
                    try {
                        //System.out.println(newSel.fileName);
                        ivStudent.setImage(new Image(new FileInputStream(newSel.fileName)));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ivStudent.setImage(null);
                }
            } else {
                ivStudent.setImage(null);
            }
        });

    }

    public class StudentAttend {

        String id;
        String name;
        HashMap<LocalDate, LocalTime> attend = new HashMap<>();

        public boolean getTmp() {
            return (true);
        }

        public String getName() {
            return (name);
        }

        public String getId() {
            return (id);
        }

        public String fileName;

        public String getAttend(LocalDate date) {
            LocalTime time = attend.get(date);
            if (time == null) {
                return ("XXX");
            } else {
                return ("present");
            }
        }

        public String getAttendPercent(int days) {
            if (days != 0)
            {
            return (String.format("%.2f%%", (attend.size() / (double) days) * 100));
            }
            else
            {
            return("N/A");
            }
        }
    }

}
