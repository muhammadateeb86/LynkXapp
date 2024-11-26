package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import database.MySql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class manageGroup implements Initializable {

    @FXML
    private TextField groupNameField; // Input field for group name

    @FXML
    private TextArea groupDescriptionArea; // Input field for group description

    @FXML
    private ListView<String> membersListView; // ListView for displaying group members

    @FXML
    private Button addMemberButton; // Button to add a member

    @FXML
    private Button removeMemberButton; // Button to remove a member

    @FXML
    private Button saveSettingsButton; // Button to save group settings

    @FXML
    private Button messagePanelButton; // Button to go to the message panel

    private String groupId; // The current group being managed

    private ObservableList<String> members; // Observable list of members

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        members = FXCollections.observableArrayList();
        membersListView.setItems(members);

        // Dummy initialization (replace with dynamic group ID)
        groupId = "G101"; // Example group ID
        //loadGroupDetails();
        //loadGroupMembers();
    }

    /*
    // Load group details
    private void loadGroupDetails() {
        GroupModel group = GroupModel.getGroupDetails(groupId);
        if (group != null) {
            groupNameField.setText(group.getGroupName());
            groupDescriptionArea.setText(group.getGroupDescription());
        }
    }

    // Load group members
    private void loadGroupMembers() {
        List<String> memberList = GroupModel.getGroupMembers(groupId);
        members.setAll(memberList);
    }

    // Add a member to the group
    @FXML
    private void addMember(ActionEvent event) {
        String newMemberId = "Enter logic to get new member ID"; // Replace with actual input logic
        if (GroupModel.addMemberToGroup(groupId, newMemberId)) {
            members.add(newMemberId);
        } else {
            System.out.println("Failed to add member.");
        }
    }

    // Remove a selected member from the group
    @FXML
    private void removeMember(ActionEvent event) {
        String selectedMember = membersListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null && GroupModel.removeMemberFromGroup(groupId, selectedMember)) {
            members.remove(selectedMember);
        } else {
            System.out.println("Failed to remove member.");
        }
    }

    // Save updated group settings
    @FXML
    private void saveSettings(ActionEvent event) {
        String newGroupName = groupNameField.getText().trim();
        String newGroupDescription = groupDescriptionArea.getText().trim();

        if (GroupModel.updateGroupDetails(groupId, newGroupName, newGroupDescription)) {
            System.out.println("Group settings updated successfully.");
        } else {
            System.out.println("Failed to update group settings.");
        }
    }

    // Navigate to the message panel
    @FXML
    private void openMessagePanel(ActionEvent event) {
        try {
            Main m = new Main();
            m.changeScene("/groupMessage.fxml"); // Change to the group message panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
