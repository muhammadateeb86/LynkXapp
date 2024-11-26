package application;

import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class afterLogin {

	@FXML
    private Button backButton;
	
	@FXML
    private Button exitButton;
	
	@FXML
	private Button joinBatch;

    public void userLogOut(ActionEvent event) throws Exception{
    	userLogOut();
    }
    
    
    public void exit(ActionEvent event) throws Exception{
    	exit();
    }
    
    //Join Batch event
    public void joinBatch(ActionEvent event) throws Exception{
    	showBatchList();
    }
    
	@FXML
    public void userLogOut() throws Exception{
    	
    	Main m = new Main();
    	m.changeScene("/Login.fxml");
    }

	@FXML
    public void exit() throws Exception{
    	
    	Main m = new Main();
    	m.changeScene("/Login.fxml");
    }
	
	@FXML
    public void showBatchList() throws Exception{
    	
    	Main m = new Main();
    	m.changeScene("/Batches.fxml");
    }
    
}
