package database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import application.StudentSession;
import classes.Student;
import classes.UserProfile;
import javafx.scene.control.Alert.AlertType;

public class MySql {
	
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/lynkx";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345678";

    public MySql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish a database connection.");
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection.");
                e.printStackTrace();
            }
        }
    }
    
    //Student find in the database
    public Student findStudent(String userId) {
        if (connection == null) {
            System.err.println("Database connection is not established.");
            return null;
        }

        String query = "SELECT * FROM student WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("batch")
                );
            } else {
                System.out.println("Student not found with ID: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student.");
            e.printStackTrace();
        }
        return null;
    }

    
	    // Method to log messages
	    public void log(String message) {
	        String insertQuery = "INSERT INTO logs (log_message) VALUES (?)";
	
	        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	            // Set the log message
	            preparedStatement.setString(1, message);
	
	            // Execute the query
	            preparedStatement.executeUpdate();
	
	            System.out.println("Log saved: " + message);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Failed to save log: " + message);
	        }
	    }
	    
	    //adding student to the group
	    public boolean addStudentToGroup(String groupName,String studentID) {
	    	if (groupName != null) {
	            String query = "INSERT INTO group_members (group_name, student_id) VALUES (?, ?)";
	            try (PreparedStatement ps = connection.prepareStatement(query)) {
	                ps.setString(1, groupName);
	                ps.setString(2, studentID);
	                ps.executeUpdate();
	                
	            } catch (SQLException e) {
	                e.printStackTrace();	            }
	            return true;
	        } else {
                 return false;
	        }
	    }
	    
	 // Add student to the batch
	    public boolean addStudentToBatch(String studentID, String selectedBatch) {
	        String query = "INSERT INTO batch_members (batch_name, user_id) VALUES (?, ?)";
	        try (PreparedStatement ps = connection.prepareStatement(query)) {
	            ps.setString(1, selectedBatch);  // Setting batch name
	            ps.setString(2, studentID);      // Setting student ID

	            int rowsInserted = ps.executeUpdate();
	            return rowsInserted > 0; // Returns true if the insert was successful
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false; // Returns false if an exception occurs
	        }
	    }
	    
	   //Get batches
	    public List<String> getBatchesName() {
	        List<String> batchList = new ArrayList<>();
	        String query = "SELECT batch_name FROM batches";

	        try (PreparedStatement statement = connection.prepareStatement(query);
	             ResultSet resultSet = statement.executeQuery()) {

	            while (resultSet.next()) {
	                batchList.add(resultSet.getString("batch_name"));
	            }

	            if (batchList.isEmpty()) {
	                System.out.println("No batches available.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error fetching batch names.");
	            e.printStackTrace();
	        }
	        return batchList;
	    }

	     
	     //Get batches for the user joined
	     public List<String> getBatchesForUser(String userId) {
	    	    List<String> batches = new ArrayList<>();
	    	    String query = "SELECT batch_name FROM batch_members WHERE user_id = ?";
	    	    try (PreparedStatement ps = connection.prepareStatement(query)) {
	    	        ps.setString(1, userId);
	    	        ResultSet rs = ps.executeQuery();

	    	        while (rs.next()) {
	    	            batches.add(rs.getString("batch_name"));
	    	        }
	    	    } catch (SQLException e) {
	    	        e.printStackTrace();
	    	    }
	    	    return batches;
	    	}

	 // Create group
	  // Create group
	     public boolean createGroup(String groupName, String groupDescription, String userIds) {
	    	    String createdBy = StudentSession.getInstance().getStudentID();
	    	    String insertGroupQuery = "INSERT INTO groups (group_name, description, created_by) VALUES (?, ?, ?)";
	    	    String addGroupMembersQuery = "INSERT INTO group_members (group_id, user_id) VALUES (?, ?)";

	    	    try {
	    	        connection.setAutoCommit(false); // Start transaction

	    	        // Create the group
	    	        int groupId;
	    	        try (PreparedStatement groupStmt = connection.prepareStatement(insertGroupQuery, Statement.RETURN_GENERATED_KEYS)) {
	    	            groupStmt.setString(1, groupName);
	    	            groupStmt.setString(2, groupDescription);
	    	            groupStmt.setString(3, createdBy);
	    	            groupStmt.executeUpdate();

	    	            ResultSet rs = groupStmt.getGeneratedKeys();
	    	            if (rs.next()) {
	    	                groupId = rs.getInt(1);
	    	            } else {
	    	                throw new SQLException("Failed to retrieve group ID.");
	    	            }
	    	        }

	    	        // Add group members
	    	        try (PreparedStatement memberStmt = connection.prepareStatement(addGroupMembersQuery)) {
	    	            memberStmt.setInt(1, groupId);
	    	            for (String userId : userIds.split(",")) {
	    	                memberStmt.setString(2, userId.trim());
	    	                memberStmt.addBatch();
	    	            }
	    	            memberStmt.executeBatch();
	    	        }

	    	        connection.commit(); // Commit transaction
	    	        return true;
	    	    } catch (SQLException e) {
	    	        System.err.println("Error during group creation.");
	    	        e.printStackTrace();
	    	        try {
	    	            connection.rollback(); // Rollback in case of error
	    	        } catch (SQLException rollbackEx) {
	    	            rollbackEx.printStackTrace();
	    	        }
	    	        return false;
	    	    } finally {
	    	        try {
	    	            connection.setAutoCommit(true); // Reset auto-commit
	    	        } catch (SQLException ex) {
	    	            ex.printStackTrace();
	    	        }
	    	    }
	    	}


	    

	    //get available groups from the database
	     public List<String> getAvailableGroups() {
	    	    List<String> availableGroups = new ArrayList<>();
	    	    String currentUserId = StudentSession.getInstance().getStudentID();
	    	    String query = "SELECT g.id, g.group_name FROM groups g "
	    	                 + "LEFT JOIN group_members gm ON g.id = gm.group_id "
	    	                 + "WHERE gm.user_id IS NULL OR gm.user_id != ?";
	    	    
	    	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	    	        stmt.setString(1, currentUserId);  // Set the current user ID
	    	        ResultSet rs = stmt.executeQuery();
	    	        
	    	        while (rs.next()) {
	    	            String groupName = rs.getString("group_name");
	    	            availableGroups.add(groupName);
	    	        }
	    	    } catch (SQLException e) {
	    	        e.printStackTrace();
	    	    }
	    	    
	    	    return availableGroups;
	    	}


	    //add user to the group
	    public boolean addUserToGroup(String groupId, String userId) {
	        String query = "INSERT INTO group_members (group_id, user_id) VALUES (?, ?)";
	        
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            
	            // Set the group ID and user ID in the prepared statement
	            stmt.setString(1, groupId);
	            stmt.setString(2, userId);
	            
	            // Execute the insert query
	            int rowsAffected = stmt.executeUpdate();
	            
	            // If one row is affected, the user has been successfully added to the group
	            if (rowsAffected == 1) {
	                return true;
	            } else {
	                return false;  // Something went wrong, user not added
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;  // Return false in case of an error
	        }
	    }
 
	   
	    // Method to get the groups a user is part of
	    public List<String> getUserGroups(String userId) {
	        List<String> groups = new ArrayList<>();
	        String query = "SELECT group_name FROM group_members gm " +
	                       "JOIN user_groups g ON gm.user_id = g.user_id " +
	                       "WHERE gm.user_id = ?";

	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, userId);
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                groups.add(rs.getString("group_name"));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error fetching user groups.");
	            e.printStackTrace();
	        }
	        return groups;
	    }



	    
	    public List<String> getMessages(String userId, String contactName) throws SQLException {
	        // Query to retrieve messages where recipient_id matches the contactName
	        String query = """
	            SELECT sender_id, message_text 
	            FROM messages 
	            WHERE (sender_id = ? AND recipient_id = ?) 
	               OR (sender_id = ? AND recipient_id = ?) 
	            ORDER BY timestamp
	        """;

	        try (var statement = connection.prepareStatement(query)) {
	            // Set query parameters
	            statement.setString(1, userId);
	            statement.setString(2, contactName); // Matches recipient_id to contactName
	            statement.setString(3, contactName); // Matches sender_id to contactName
	            statement.setString(4, userId);

	            ResultSet resultSet = statement.executeQuery();

	            // Collect messages in a list
	            List<String> messages = new ArrayList<>();
	            while (resultSet.next()) {
	                String senderId = resultSet.getString("sender_id");
	                String message = resultSet.getString("message_text");

	                // Add messages formatted based on sender
	                if (senderId.equals(userId)) {
	                    messages.add("You: " + message);
	                } else {
	                    messages.add(contactName + ": " + message);
	                }
	            }

	            return messages; // Return all collected messages
	        } catch (SQLException e) {
	            System.err.println("Error retrieving messages: " + e.getMessage());
	            throw e; // Re-throw exception for further handling
	        }
	    }




	    public boolean sendMessage(String senderId, String receiverId, String message) throws SQLException {
	        // Update query to use correct column names
	        String query = "INSERT INTO messages (sender_id, recipient_id, message_text, timestamp) VALUES (?, ?, ?, NOW())";
	        var statement = connection.prepareStatement(query);
	        statement.setString(1, senderId);
	        statement.setString(2, receiverId); // Changed from receiver_id to recipient_id
	        statement.setString(3, message);

	        return statement.executeUpdate() > 0; // Returns true if the message was sent successfully
	    }

	    public void loadUserData(String studentId) {
	        String query = "SELECT student_id, password, batch, name, profile_picture_path FROM users WHERE student_id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            
	            // Pass the student ID as a parameter
	            statement.setString(1, studentId);
	            ResultSet resultSet = statement.executeQuery();

                UserProfile uP = new UserProfile();

	            if (resultSet.next()) {
	                String studentIdFromDb = resultSet.getString("student_id");
	                String password = resultSet.getString("password");
	                String batch = resultSet.getString("batch");
	                String name = resultSet.getString("name");
	                String profilePicturePath = resultSet.getString("profile_picture_path");
	                String username = resultSet.getString("name"); // Assuming 'name' is being used as the username

	                // Set values (You could also set them in a UserProfile or another object)
	                uP.setRollNumber(studentIdFromDb);
	                uP.setBatchId(batch);
	                uP.setProfilePicturePath(profilePicturePath);
	                uP.setUsername(username); // Setting the username field
	            } else {
	                // Defaults if no user found
	            	uP.setRollNumber("");
	            	uP.setBatchId("");
	            	uP.setProfilePicturePath("images/default_profile.png");
	            	uP.setUsername(""); // Default username
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    
	    public void saveUserData(String studentId, String newUsername, File selectedFile) {
	        try {
	            // Get the current profile picture path (if available)
	            String currentProfilePicturePath = getProfilePicturePathFromDatabase(studentId);
	            
	            // Update the username and profile picture (if provided) in the database
	            String updateQuery = "UPDATE users SET name = ?, profile_picture_path = ? WHERE student_id = ?";
	            
	            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {

	                // Set the new username
	                statement.setString(1, newUsername);

	                // Handle profile picture update if a new one is provided
	                String newProfilePicturePath = currentProfilePicturePath; // Default to current if no new picture is uploaded
	                if (selectedFile != null) {
	                    // Save the new profile picture file to the system
	                    File newProfilePicture = new File("profile_pictures/" + studentId + ".png");
	                    if (newProfilePicture.exists()) {
	                        newProfilePicture.delete(); // Delete the old profile picture if it exists
	                    }
	                    Files.copy(
	                        selectedFile.toPath(),
	                        newProfilePicture.toPath(),
	                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
	                    );
	                    newProfilePicturePath = "profile_pictures/" + studentId + ".png"; // Set the new profile picture path
	                }

	                // Set the profile picture path in the database
	                statement.setString(2, newProfilePicturePath);

	                // Set the student ID for the WHERE clause
	                statement.setString(3, studentId);

	                // Execute the update in the database
	                int rowsUpdated = statement.executeUpdate();
	                if (rowsUpdated > 0) {
	                    System.out.println("User data updated successfully.");
	                } else {
	                    System.out.println("No data found to update.");
	                }

	            }
	        } catch (IOException | SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private String getProfilePicturePathFromDatabase(String studentId) {
	        // Query to get the current profile picture path
	        String query = "SELECT profile_picture_path FROM users WHERE student_id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, studentId);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    return resultSet.getString("profile_picture_path");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return ""; // Return an empty string if no path is found (default behavior)
	    }



	    public void saveProfilePicture(String studentId, File selectedFile) {
	        try {
	            // Ensure directory exists
	            File profilePicDir = new File("profile_pictures");
	            if (!profilePicDir.exists()) {
	                profilePicDir.mkdirs();  // Create directory if it doesn't exist
	            }

	            // Save the profile picture file locally
	            File newProfilePicture = new File(profilePicDir, studentId + ".png");
	            if (newProfilePicture.exists()) {
	                newProfilePicture.delete(); // Delete old profile picture if it exists
	            }
	            Files.copy(
	                selectedFile.toPath(),
	                newProfilePicture.toPath(),
	                java.nio.file.StandardCopyOption.REPLACE_EXISTING
	            );
	            String newProfilePicturePath = "profile_pictures/" + studentId + ".png";

	            // Update the profile picture path in the database
	            String query = "UPDATE users SET profile_picture_path = ? WHERE student_id = ?";
	            try (PreparedStatement statement = connection.prepareStatement(query)) {
	                statement.setString(1, newProfilePicturePath);  // Set the new path
	                statement.setString(2, studentId);               // Set the student ID
	                statement.executeUpdate();                        // Execute update in DB
	            }
	        } catch (IOException | SQLException e) {
	            e.printStackTrace();
	        }
	    }




	}

