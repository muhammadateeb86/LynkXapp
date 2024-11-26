package classes;

public class Student {

    private String studentId;
    private String name;
    private String password;
    private String batchId;

    // Constructor
    public Student(String studentId, String name, String password, String batchId) {
        this.studentId = studentId;
        this.name = name;
        this.password = password;
        this.batchId = batchId;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    // Delegate login to the service
    public boolean login(AuthenticationService authService) {
        return authService.authenticate(studentId, password);
    }

    // Delegate batch join functionality to the BatchService
    public String requestToJoinBatch(String selectedBatchId, BatchService batchService, NotificationService notificationService) {

    	this.batchId = selectedBatchId;
            batchService.addUserToBatch(studentId, batchId);
            //notificationService.notifyBatchMembers(batchId, "New student has joined the batch.");
            return selectedBatchId;
        
    }
}
