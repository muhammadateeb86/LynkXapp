package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import classes.GroupService;
import classes.Student;
import database.MySql;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class createGroup {

    @FXML
    private ListView<String> groupListView;

    @FXML
    private Label selectedGroupLabel;

    @FXML
    private Button createGroupButton;

    @FXML
    private Button joinGroupButton;

    @FXML
    private Button submitGroupButton;

    @FXML
    private TextField groupNameField;

    @FXML
    private TextField groupDescriptionField;

    private Connection connection;


    // Load groups from the database into the ListView
    private void loadGroups() {
        String query = "SELECT group_name FROM groups";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                groupListView.getItems().add(rs.getString("group_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Join a selected group
    @FXML
    public void joinGroup(ActionEvent event) {
        String selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        
        GroupService gS = new GroupService();
        gS.joinGroup(StudentSession.getInstance().getStudentID(),selectedGroup);
        
    }

    // Prepare to create a new group
    @FXML
    public void prepareCreateGroup(ActionEvent event) {
        groupNameField.setVisible(true);
        groupDescriptionField.setVisible(true);
        submitGroupButton.setVisible(true);
    }

    // Submit a new group
    @FXML
    public void createGroup(ActionEvent event) {
        String groupName = groupNameField.getText();
        String groupDescription = groupDescriptionField.getText();
        String studentID = StudentSession.getInstance().getStudentID();

        if (groupName.isEmpty() || groupDescription.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Group name and description cannot be empty.");
            return;
        }

        
        GroupService gS = new GroupService();
        gS.createGroup(groupName,groupDescription,studentID);
        
        
    }

    // Notify group members in future if you have to cha
   /* private void notifyMembers(String groupName, String message) {
        String query = "SELECT student_id FROM group_members WHERE group_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String studentID = rs.getString("student_id");
                System.out.println("Notification sent to " + studentID + ": " + message);
                // Implement actual notification logic here (e.g., email, SMS, app notification)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    // Show alerts to the user
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
