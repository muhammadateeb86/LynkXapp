package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class groupMessage {

    private String messageContent;
    private String senderId;
    private String groupId;
    private String timestamp;

    // Constructor
    public groupMessage(String messageContent, String senderId, String groupId) {
        this.messageContent = messageContent;
        this.senderId = senderId;
        this.groupId = groupId;
    }

    // Default constructor
    public groupMessage() {}

    // Getters and setters
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    
    /*
    // Store a message in the database
    public boolean sendMessageToGroup() {
    
        String query = "INSERT INTO group_messages (group_id, sender_id, message_content, sent_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = MySql.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, this.groupId);
            ps.setString(2, this.senderId);
            ps.setString(3, this.messageContent);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0; // Return true if the message was stored successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }

    // Retrieve messages for a group
    public static List<groupMessage> getMessagesForGroup(String groupId) {
        String query = "SELECT sender_id, message_content, sent_at FROM group_messages WHERE group_id = ? ORDER BY sent_at ASC";
        List<groupMessage> messages = new ArrayList<>();

        try (Connection conn = MySql.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, groupId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                groupMessage message = new groupMessage();
                message.setSenderId(rs.getString("sender_id"));
                message.setMessageContent(rs.getString("message_content"));
                message.setTimestamp(rs.getString("sent_at"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages; // Return the list of messages
    }*/
}
