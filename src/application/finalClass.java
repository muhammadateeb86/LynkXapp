package application;

import javafx.scene.control.Button; // NOT java.awt.Button
import javafx.scene.control.ChoiceDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import classes.BatchService;
import classes.ChatMessage;
import database.MySql;

public class finalClass implements Initializable {

    @FXML
    private ListView<HBox> contactsList;

    @FXML
    private ListView<String> chatMessages;
    
    @FXML
    private TextField messageInput;

    @FXML
    private Button joinBatch;

    @FXML
    private Button createGroup;

    @FXML
    private Button backButton;
    
    @FXML
    private Button sendButton;
    
    @FXML
    private Button profileButton;

    private final HashMap<String, List<String>> chatHistory = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (contactsList == null) {
            System.err.println("contactsList is null! Ensure FXML is linked correctly.");
        } else {
            loadInitialContacts();
        }

        contactsList.setOnMouseClicked(this::onContactSelected);
    }

    private Image loadImageForBatchOrGroup(String name) {
        // Check if it's a batch or group
        if (name.startsWith("Batch")) {
            return loadImage("/images/joinBatch.png");
        } else if (name.startsWith("Group")) {
            return loadImage("/images/joinBatch.png");
        } else {
            return loadImage("/images/joinBatch.png");  // Default image for other cases
        }
    }

    private void loadInitialContacts() {
        try {
            MySql db = new MySql();
            String userId = StudentSession.getInstance().getStudentID();
            
            if (!db.isConnected()) {
                JOptionPane.showMessageDialog(null, "Database connection not established.");
                return;
            }

            // Retrieve the list of batches the user has joined
            List<String> joinedBatches = db.getBatchesForUser(userId);
            // Add each batch to the contact list with the fixed image
            for (String batch : joinedBatches) {
                addContact(batch, loadImageForBatchOrGroup(batch));
            }

            // Similarly, load and add groups (if necessary)
            List<String> joinedGroups = db.getUserGroups(userId);
            for (String group : joinedGroups) {
                addContact(group, loadImageForBatchOrGroup(group));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image loadImage(String resourcePath) {
        InputStream imageStream = getClass().getResourceAsStream(resourcePath);
        if (imageStream == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }
        return new Image(imageStream);
    }

    public void addContact(String name, Image profileImage) {
        HBox contactBox = new HBox();
        contactBox.setSpacing(10);

        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(40);
        profileImageView.setFitWidth(40);

        VBox contactDetails = new VBox();
        Text contactName = new Text(name);
        contactName.setStyle("-fx-font-size: 14px; -fx-fill: #00a884;");
        Text latestMessage = new Text("Tap to view chat...");
        latestMessage.setStyle("-fx-font-size: 12px; -fx-fill: #555;");

        contactDetails.getChildren().addAll(contactName, latestMessage);
        contactBox.getChildren().addAll(profileImageView, contactDetails);

        contactsList.getItems().add(contactBox);
    }

    @FXML
    private void onContactSelected(MouseEvent event) {
        // Get the selected contact from the ListView
        HBox selectedContact = contactsList.getSelectionModel().getSelectedItem();
        
        if (selectedContact == null) {
            return; // Exit if no contact is selected
        }

        // Extract contact details
        VBox contactDetails = (VBox) selectedContact.getChildren().get(1);
        Text contactNameText = (Text) contactDetails.getChildren().get(0);
        String contactName = contactNameText.getText();

        // Clear the message area before loading new messages
        chatMessages.getItems().clear();

        try {
            // Create and connect to the database
            MySql db = new MySql();
            if (!db.isConnected()) {
                JOptionPane.showMessageDialog(null, "Database connection not established.");
                return;
            }

            // Get the messages from the database
            String userId = StudentSession.getInstance().getStudentID();
            List<String> messages = db.getMessages(userId, contactName);

            if (messages.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No messages found.");
            }

            // Append each message to the message area
            for (String message : messages) {
                chatMessages.getItems().add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load messages.");
        }
    }





    @FXML
    public void onSendMessage(ActionEvent event) {
        String message = messageInput.getText();
        if (message != null && !message.trim().isEmpty()) {

            HBox selectedContact = contactsList.getSelectionModel().getSelectedItem();
            if (selectedContact == null) {
                JOptionPane.showMessageDialog(null, "No contact selected.");
                return;
            }

            VBox contactDetails = (VBox) selectedContact.getChildren().get(1);
            Text contactNameText = (Text) contactDetails.getChildren().get(0);
            String contactName = contactNameText.getText();

            try {
                MySql db = new MySql();
                if (!db.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Database connection not established.");
                    return;
                }

                String userId = StudentSession.getInstance().getStudentID();

                // Send the message to the database
                boolean isMessageSent = db.sendMessage(userId, contactName, message);

                if (isMessageSent) {
                    // Add the sent message in the chat window (you: message)
                    chatMessages.getItems().add("You: " + message);

                    messageInput.clear();

                    // Refresh the messages to update the chat window
                    List<String> updatedMessages = db.getMessages(userId, contactName);
                    chatMessages.getItems().clear(); // Clear current chat messages

                    // Add all the retrieved messages
                    for (String msg : updatedMessages) {
                        chatMessages.getItems().add(msg);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to send the message.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error sending message.");
            }
        }
    }






    @FXML
    public void onJoinBatchClick(ActionEvent event) {
        // 1. Establish database connection
        MySql db = new MySql();
        if (!db.isConnected()) {
            JOptionPane.showMessageDialog(null, "Database connection not established.");
            return;
        }

        try {
            // 2. Collect batches into a list
            String userId = StudentSession.getInstance().getStudentID();

            // Get batches the user has already joined
            List<String> joinedBatches = db.getBatchesForUser(userId);

            // Get available batches and exclude already joined ones
            List<String> batchList = db.getBatchesName();
            batchList.removeAll(joinedBatches);

            // 3. Display a prompt with batch options
            ChoiceDialog<String> dialog = new ChoiceDialog<>(batchList.get(0), batchList);
            dialog.setTitle("Join a Batch");
            dialog.setHeaderText("Select a Batch");
            dialog.setContentText("Available Batches:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String selectedBatch = result.get();

                // 4. Add the user to the selected batch
                BatchService bS = new BatchService();
                boolean isAdded = bS.addUserToBatch(userId, selectedBatch);
                if (!isAdded) {
                    JOptionPane.showMessageDialog(null, "Failed to join the batch.");
                    return;
                }

                // Add the batch to the contact list
                addContact(selectedBatch, loadImage("/images/logo.png"));

                // Notify the user
                JOptionPane.showMessageDialog(null, "You have joined the batch: " + selectedBatch);

                // 5. Fetch and display messages for the selected batch
                List<String> messages = db.getMessages(userId, selectedBatch);

                // Clear the message area and display the fetched messages
                chatMessages.getItems().clear();
                for (String message : messages) {
                	chatMessages.getItems().add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while joining the batch or fetching messages.");
        }
    }



    @FXML
    public void onCreateGroupClick(ActionEvent event) {
        // Show dialog asking user to join or create a group
        String[] options = {"Join Group", "Create Group"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Would you like to join an existing group or create a new group?",
                "Group Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]  // Default option is "Join Group"
        );

        if (choice == 0) {
            // User chose to join a group
            joinGroup();
        } else if (choice == 1) {
            // User chose to create a new group
            createGroup();
        } else {
            JOptionPane.showMessageDialog(null, "No action selected!");
        }
    }

    private void joinGroup() {
        MySql db = new MySql();
        if (!db.isConnected()) {
            JOptionPane.showMessageDialog(null, "Database connection not established.");
            return;
        }

        // Get list of available groups
        List<String> availableGroups = db.getAvailableGroups(); // Assuming this method exists
        if (availableGroups.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No groups available to join.");
            return;
        }

        // Display the available groups in a dialog
        String groupName = (String) JOptionPane.showInputDialog(
                null,
                "Select a group to join:",
                "Join Group",
                JOptionPane.QUESTION_MESSAGE,
                null,
                availableGroups.toArray(),
                availableGroups.get(0)
        );

        if (groupName != null) {
            // Add user to the selected group
            String userId = StudentSession.getInstance().getStudentID();
            boolean isJoined = db.addUserToGroup(userId, groupName); // Assuming this method exists
            if (isJoined) {
                JOptionPane.showMessageDialog(null, "Successfully joined group: " + groupName);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to join the group.");
            }
        }
    }

    private void createGroup() {
        // Prompt user for group details
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Group Picture");
        File file = fileChooser.showOpenDialog(new Stage());

        String groupName = JOptionPane.showInputDialog("Enter Group Name:");
        String groupDescription = JOptionPane.showInputDialog("Enter Group Description:");
        String userIds = JOptionPane.showInputDialog("Enter User IDs (comma-separated):");

        // Insert the group into the groups table
        MySql db = new MySql();
        if (!db.isConnected()) {
            JOptionPane.showMessageDialog(null, "Database connection not established.");
            return;
        }

        // Assuming you have a method that inserts the group into the database
        db.createGroup(groupName, groupDescription, userIds);

        // Add the group to the contact list with the fixed image
        addContact(groupName + " (Group)", loadImageForBatchOrGroup(groupName));

        // Confirm group creation
        JOptionPane.showMessageDialog(null, "Group " + groupName + " created successfully!");
    }




    @FXML
    public void onBackButtonClick(ActionEvent event) throws Exception {
        Main main = new Main();
        main.changeScene("/login.fxml");
    }
    
	/*
	 * // Event handler for profileButton\ private void
	 * onProfileButtonClick(ActionEvent event) throws Exception { profilePage(); }
	 */    
    
    @FXML
    private void profilePage() throws Exception {
    	Main m = new Main();
      	m.changeScene("/profile.fxml");
    }
}
