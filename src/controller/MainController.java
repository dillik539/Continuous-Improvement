package controller;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
/**
 * 
 * @author Dilli Khatiwoda
 * main controller class. Controls other sub-controllers.
 *
 */

public class MainController {
	private Stage primaryStage;
	private BorderPane root;
	private MenuItem loginMenuItem;
	private MenuItem logoutMenuItem;
	private Label userLabel;
	
	
	public MainController(Stage primaryStage) {
		this.primaryStage = primaryStage;
		root = new BorderPane();
		setupMenuBar();
		setupWelcomePage();
	}
	
	private void setupMenuBar() {
		//TO DO: add login and logout menus with their functionalities.
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("User");
	}
	private void setupWelcomePage() {
		userLabel.setText("Welcome! Please login to submit your ideas.");
		root.setCenter(userLabel);
			
	}
	public BorderPane getView() {
		return root;
	}
}