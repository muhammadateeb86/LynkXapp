package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import classes.Student;
import classes.AuthenticationService;
import classes.BatchService;
import classes.NotificationService;

public class Login {

    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private Label wrongLogIn;

    @FXML
    private Button loginButton;

    private AuthenticationService authService;
    private BatchService batchService;
    private NotificationService notificationService;

    public Login() {
        authService = new AuthenticationService();
        batchService = new BatchService();
        notificationService = new NotificationService();
    }

    @FXML
    public void authenticateLogin() throws Exception {
        String userId = username.getText();
        String userPassword = password.getText();
        
        Student student = new Student(userId, null, userPassword, null);
        
        if (!student.login(authService)) {
            wrongLogIn.setVisible(true);
            System.out.println("Please register.");
        } else {
            wrongLogIn.setVisible(false);
            StudentSession.getInstance().setStudentID(userId);
            Main m = new Main();
            m.changeScene("/chat.fxml");
        }
    }
}
