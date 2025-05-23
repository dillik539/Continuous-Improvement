package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
	private MenuBar menuBar;
	private Button loginButton;
	private Button logoutButton;
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
	    menuBar = new MenuBar();
	    menuBar.setStyle("-fx-background-color: #ADD8E6;"); // Optional if you want to color MenuBar too

	    HBox menuBox = new HBox(10);
	    menuBox.setPadding(new Insets(5));
	    menuBox.setAlignment(Pos.CENTER_LEFT);
	    menuBox.setStyle("-fx-background-color: #ADD8E6;"); // Color the bar
	       
	    loginButton = new Button("Login");
	    logoutButton = new Button("Logout");
	    logoutButton.setDisable(true);

	    // Optional button styling
	    String buttonStyle = "-fx-background-color: white; -fx-text-fill: #005f87; -fx-font-weight: bold;";
	    loginButton.setStyle(buttonStyle);
	    logoutButton.setStyle(buttonStyle);

	    loginButton.setOnAction(e -> openLoginWindow());
	    logoutButton.setOnAction(e -> handleLogout());

	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    userLabel = new Label();
	    userLabel.setStyle("-fx-text-fill: #003f5c; -fx-font-size: 14px; -fx-font-weight: bold;");

	    menuBox.getChildren().addAll(loginButton, logoutButton, spacer, userLabel);

	    VBox topBox = new VBox(menuBar, menuBox);
	    root.setTop(topBox);
	}


	private void setupWelcomePage() {
		if (userLabel != null) {
			userLabel.setText(""); //Clear the top-right label
		}
		Label welcomeLabel = new Label();
		welcomeLabel.setText("Welcome to the Idea Submission app!\nPlease login to submit your ideas.");
		root.setCenter(welcomeLabel);//separate label for center content
			
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
		loginButton.setDisable(true);
		logoutButton.setDisable(false);
		
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
		loginButton.setDisable(false);
		logoutButton.setDisable(true);
		setupWelcomePage();
	}
	
	public BorderPane getView() {
		return root;
	}
}