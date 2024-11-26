package application;

import java.net.URL;
import java.util.ResourceBundle;

import database.MySql;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class viewClass implements Initializable {

    @FXML
    private TextField profileIDField; // Field for entering the profile ID

    @FXML
    private Label profileInfoLabel; // Label to display profile information

    @FXML
    private Button requestAccessButton; // Button to request access to the profile

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize with default values or actions if needed
        profileInfoLabel.setText("Enter a Profile ID and click 'Request Access'");
    }

    @FXML
    private void requestAccess(ActionEvent event) {
        String profileID = profileIDField.getText().trim();

        if (profileID.isEmpty()) {
            showError("Profile ID cannot be empty.");
            return;
        }

        // Request access to the profile
        if (grantAccessToProfile(profileID)) {
            String profileInfo = getProfileInfo(profileID);
            if (profileInfo != null) {
                profileInfoLabel.setText(profileInfo);
                notifyMember(profileID);
            } else {
                showError("Profile not found.");
            }
        } else {
            showError("Access denied to the requested profile.");
        }
    }

    // Simulate granting access to the profile
    private boolean grantAccessToProfile(String profileID) {
        // Add logic to verify access based on permissions, e.g., database checks
        return profileID.startsWith("P"); // Example: Allow access if ID starts with 'P'
    }

    // Retrieve profile information from the database
    private String getProfileInfo(String profileID) {
        String query = "SELECT name, email, role FROM profiles WHERE profile_id = ?";
       /* try (var conn = MySql.getConnection(); var ps = conn.prepareStatement(query)) {
            ps.setString(1, profileID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");
                return "Name: " + name + "\nEmail: " + email + "\nRole: " + role;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    // Notify the member whose profile was accessed
    private void notifyMember(String profileID) {
        // Add logic to notify the member, e.g., sending a notification via database
        System.out.println("Notification sent to member with ID: " + profileID);
    }

    // Show error alert
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
