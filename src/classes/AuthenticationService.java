package classes;
import java.util.*;

import database.MySql;

public class AuthenticationService {

    // Simulated database of student credentials
    private Map<String, String> studentCredentials;
    public MySql mySql;
    // Constructor
    public AuthenticationService() {
         mySql = new MySql();
        studentCredentials = new HashMap<>();
        // Adding some dummy data for testing
        studentCredentials.put("student001", "password123");
        studentCredentials.put("student002", "password456");
    }

    /**
     * Verifies if the studentId and password match.
     * 
     * @param studentId The ID of the student.
     * @param password The password entered by the student.
     * @return true if credentials are correct, false otherwise.
     */
    public boolean authenticate(String studentId, String password) {
    	
        Student student = mySql.findStudent(studentId);
        
        if (student != null && student.getPassword().equals(password)) {
            // Login success, proceed to next scene or function
            System.out.println("Login successful for: " + student.getName());
            return true;
            // Switch scene or store session details as needed
        } 
            // Show error message
            return false;
    }

    /**  Use it later if needed
     * Adds a new student to the simulated credential store.
     * 
     * @param studentId The ID of the student.
     * @param password The password for the student.
     *
    public void registerStudent(String studentId, String password) {
        studentCredentials.put(studentId, password);
    } */
}
