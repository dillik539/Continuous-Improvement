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
		
		loginMenuItem = new MenuItem("Login");
		logoutMenuItem = new MenuItem("Logout");
		logoutMenuItem.setDisable(true);
		
		loginMenuItem.setOnAction(e -> openLoginWindow());
		logoutMenuItem.setOnAction(e -> handleLogout());
		
		menu.getItems().addAll(loginMenuItem, logoutMenuItem);
		menuBar.getMenus().add(menu);
		
		userLabel = new Label("Welcome to the Idea Submission App");
		root.setTop(menuBar);
		root.setCenter(userLabel);
	}
	private void setupWelcomePage() {
		userLabel.setText("Welcome! Please login to submit your ideas.");
		root.setCenter(userLabel);
			
	}
	private void openLoginWindow() {
		//TO DO: Create LoginController class
		LoginController loginController = new LoginController(this);
		loginController.showLoginWindow();
	}
	public void handleLogin(String username) {
		loginMenuItem.setDisable(true);
		logoutMenuItem.setDisable(false);
		//TO DO: add a label in the right of menu bar and display "Welcome, " + username message.
		
		//TO DO: Create IdeaController class
		IdeaController ideaController = new IdeaController(username);
		root.setCenter(ideaController.getView());
	}
	private void handleLogout() {
		loginMenuItem.setDisable(false);
		logoutMenuItem.setDisable(true);
		setupWelcomePage();
	}
	
	public BorderPane getView() {
		return root;
	}
}