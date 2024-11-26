package application;
	
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private static Stage stg;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			stg = primaryStage;
			primaryStage.setResizable(false);
			BorderPane root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
			Scene scene = new Scene(root,1200,675);
			scene.getStylesheets().add(getClass().getResource("/value/style.css").toExternalForm());
			primaryStage.setScene(scene);
		    primaryStage.setResizable(false); // Prevent resizing to avoid layout distortions
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeScene(String  fxml) throws Exception{
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        System.out.print("Scene changed");
	}
	public static void main(String[] args) {
		launch(args);
	}
}
