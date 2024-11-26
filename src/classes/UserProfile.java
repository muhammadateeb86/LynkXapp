package classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import application.StudentSession;
import database.MySql;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

public class UserProfile {
    private String rollNumber;
    private String batchId;
    private String profilePicturePath;
    private String username; // New field for username

    public UserProfile() {
        // Load user data from file or set defaults
        loadUserData();
    }

    // Getter and setter for rollNumber
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    // Getter and setter for batchId
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    // Getter and setter for profilePicturePath
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // This method is to load the user data from the database or file
    public void loadUserData() {
        MySql mS = new MySql();
        // Assuming loadUserData will now also set the username
        
        mS.loadUserData(StudentSession.getInstance().getStudentID()); // Pass `this` to populate the data
    }

    // Save user data with username and studentId (not handling file here)
    public void saveUserData(String studentId, String newUsername) {
        MySql mS = new MySql();
        // Save new username to the database
        this.username = newUsername;  // Set username in the current object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

          
        mS.saveUserData(studentId, newUsername,selectedFile); // Update in the database
    }

    // Save profile picture in the system and set the path
    public void saveProfilePicture(File selectedFile) {
        try {
            File newProfilePicture = new File("profile.png");
            if (newProfilePicture.exists()) {
                newProfilePicture.delete(); // Delete the old picture if it exists
            }
            // Copy the new profile picture to the system
            Files.copy(
                selectedFile.toPath(),
                newProfilePicture.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            // Update profile picture path
            this.profilePicturePath = "profile.png"; // Set the profile picture path
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
