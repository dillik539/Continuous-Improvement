package application;

import javafx.application.Application;
import javafx.scene.Scene;
import controller.MainController;
import javafx.stage.Stage;
/**
 * 
 * @author Dilli Khatiwoda
 * main controller class. This is the entry point to the program
 *
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		MainController controller = new MainController(primaryStage);
		Scene scene = new Scene(controller.getView(), 1000, 800);
		//Load the global style sheet
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		
		primaryStage.setTitle("Idea Submission App");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
public static void main (String[] args) {
	launch(args);
}
	
}

