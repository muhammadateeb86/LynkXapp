package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import classes.UserProfile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class profile implements Initializable {

    private final UserProfile userProfile = new UserProfile();

    @FXML
    private ImageView profileImageView;

    @FXML
    private TextField usernameField; // New text field for username

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load user data into UI components        // Load the profile picture
        try {
            File profilePicture = new File(userProfile.getProfilePicturePath());
            if (profilePicture.exists()) {
                profileImageView.setImage(new Image(profilePicture.toURI().toString()));
            } else {
                profileImageView.setImage(new Image(getClass().getResource("/images/profile.png").toExternalForm()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            profileImageView.setImage(new Image(getClass().getResource("/images/profile.png").toExternalForm()));
        }

        // Apply circular clipping for the profile image
        Circle circle = new Circle(75, 75, 75);
        profileImageView.setClip(circle);

        // Set initial username (if needed)
        usernameField.setText(userProfile.getUsername());  // Set the current username
    }

    @FXML
    private void changeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image newImage = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(newImage);

            // Save the new profile picture path in the model
            userProfile.saveProfilePicture(selectedFile);
        }
    }

    @FXML
    private void saveChanges() {
        // Only allow username change and not the roll number or batch ID
        String newUsername = usernameField.getText();

        // Persist data: save the updated profile picture and username
        userProfile.saveUserData(StudentSession.getInstance().getStudentID(),newUsername);  // Save changes to database (username and profile picture)
    }

    @FXML
    private void chatPage() throws Exception {
        Main m = new Main();
        m.changeScene("/chat.fxml");
    }
}
