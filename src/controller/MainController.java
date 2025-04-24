package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
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
	private String username;
	private String role;
	
	
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
		//root.setCenter(userLabel);
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

	/*
	public void handleLogin(String username) {
		loginMenuItem.setDisable(true);
		logoutMenuItem.setDisable(false);
		//TO DO: add a label in the right of menu bar and display "Welcome, " + username message.
		
		//TO DO: Create IdeaController class
		IdeaController ideaController = new IdeaController(username);
		root.setCenter(ideaController.getView());
	}
	*/
	/**
	 * Role-based page loading after login.
	 */
	public void handleLogin(User user) {
		loginMenuItem.setDisable(true);
		logoutMenuItem.setDisable(false);
		
		username = user.getUsername();
		role = user.getRole().toLowerCase();
		
		userLabel.setText("Welcome, " + username + " (" + role + ")");
		
		switch (role) {
		case "admin":
			VBox adminLayout = new VBox(10);
			
			IdeaController ideaController = new IdeaController(username);
			AdminController adminController = new AdminController();
			
			Button addUserButton = new Button("Add New User");
			addUserButton.setOnAction(e -> new AddUserController().show());
			
			adminLayout.getChildren().addAll(new Label ("Idea Submission"), ideaController.getView(), new Label ("Admin Panel"), adminController.getView(), addUserButton);
			root.setCenter(adminLayout);
			break;
			
		case "user":
			IdeaController userController = new IdeaController(username);
			root.setCenter(userController.getView());
			break;
			
		default:
			userLabel.setText("No view assigned for role: " + role);
		}
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