package application;

public class StudentSession {
    private static StudentSession instance; // Singleton instance
    private String studentID; // The student ID
   
    // Private constructor to prevent instantiation
    private StudentSession() {}

    // Static method to get the single instance
    public static StudentSession getInstance() {
        if (instance == null) {
            instance = new StudentSession();
        }
        return instance;
    }

    // Getter and Setter for studentID
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}