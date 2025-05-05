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
		//To Do: create MainController class in controller package.
		MainController controller = new MainController(primaryStage);
		Scene scene = new Scene(controller.getView(), 1000, 800);
		
		primaryStage.setTitle("Idea Submission App");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
public static void main (String[] args) {
	launch(args);
}
	
}

