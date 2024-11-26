package classes;

import java.util.*;

import database.MySql;
import javafx.scene.control.Alert.AlertType;

public class GroupService {
    private List<Group> groups;

    public GroupService() {
        this.groups = new ArrayList<>();
    }

    public List<Group> getAvailableGroups() {
        return groups;  // Return the list of available groups
    }

    public boolean validateGroupName(String groupName) {
        // Check if the group name is unique
        for (Group group : groups) {
            if (group.getGroupName().equalsIgnoreCase(groupName)) {
                return false;
            }
        }
        return true;
    }

    public void createGroup(String groupName, String description,String creatorId) {
        // Create a new group
    	MySql mySql = new MySql();
  	    boolean created = mySql.createGroup(groupName, description,creatorId);
        
  	    //Notify members here
    }

    public void joinGroup(String student_Id,String groupName) {
        
    	  // Find the group by name and add the member
    	  MySql mySql = new MySql();
    	  boolean added = mySql.addStudentToGroup(groupName, student_Id);
    	  
    	  if(added == true) {
    		  String message = "A new member" + student_Id + "has joined your group!";
              NotificationService.notifyMembers(groupName, student_Id,message);
    	  }
    	  
    }

    public List<Student> getMembers(String groupName) {
    	
    	for (Group group : groups) {
            if (group.getGroupName().equalsIgnoreCase(groupName)) {
                return group.getMembers();
            }
        }
		return null;
    }
}
