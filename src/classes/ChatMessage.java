package classes;

public class ChatMessage {
    private String userId;
    private String message;

    public ChatMessage(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        // Customize how the object will be displayed in the ListView
        return "User " + userId + ": " + message;
    }
}

