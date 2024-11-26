package classes;

import java.util.List;

import database.MySql;

public class NotificationService {

    /**
     * Sends a notification message to all members of the batch.
     * 
     * @param batchId The ID of the batch to notify.
     * @param message The notification message to be sent.
     */
    public static void notifyBatchMembers(String batchId, String message) {
    	MySql sql = new MySql();
    	sql.log("message");
        // Dummy notification sending logic
        System.out.println("Notification to batch " + batchId + ": " + message);
    }
    
    /**
     * Sends a notification message to all members of the group.
     * 
     * @param group The group whose members need to be notified.
     * @param message The notification message to be sent.
     */
    public static void notifyMembers(String groupName,String student_Id, String message) {
    	MySql sql = new MySql();
    	sql.log("message");
        // Dummy notification sending logic
        System.out.println("Notification to group " + student_Id + message);
    }
    
    public void notifyGroupMembers(List<Student> groupMembers, String message ) {
        // Notify each member that the group has been created
        for (Student member : groupMembers) {
            System.out.println("Notification sent to " + member.getName() + " about the group creation.");
        }
    }
}
