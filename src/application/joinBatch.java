package application;

import java.net.URL;
import java.util.ResourceBundle;

import database.MySql;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import classes.BatchService;
import classes.Student;

public class joinBatch implements Initializable {

    @FXML
    private ListView<String> myListView;

    @FXML
    private Label myLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button addButton; // New button for adding students to a batch

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Populate ListView with batch years
        for (int year = 2012; year <= 2024; year++) {
            myListView.getItems().add("Batch " + year);
        }

        // Update the label text when an item is selected
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    myLabel.setText(newValue);
                }
            }
        });
    }

    @FXML
    public void addToBatch(ActionEvent event) {
        String selectedBatch = myListView.getSelectionModel().getSelectedItem();
        if (selectedBatch != null) {
        	//Find student in the database
        	BatchService bactchService = new BatchService();
        	bactchService.addUserToBatch(StudentSession.getInstance().getStudentID(),selectedBatch);
            System.out.println("Student added to " + selectedBatch);
            // Additional logic can go here, such as saving the association to a database
        } else {
            System.out.println("No batch selected.");
        }
    }

    @FXML
    public void userLogOut() {
        try {
            Main m = new Main();
            m.changeScene("/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        javafx.application.Platform.exit();
    }
}
