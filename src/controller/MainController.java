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
	private Button peerRecognitionButton;
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
	    

	    HBox menuBox = new HBox(10);
	    menuBox.setPadding(new Insets(5));
	    menuBox.setAlignment(Pos.CENTER_LEFT);
	   
	       
	    loginButton = new Button("Login");
	    logoutButton = new Button("Logout");
	    peerRecognitionButton = new Button("Peer Recognition");
	    
	    logoutButton.setDisable(true);
	    peerRecognitionButton.setVisible(false);

	    //Events handlers
	    loginButton.setOnAction(e -> openLoginWindow());
	    logoutButton.setOnAction(e -> handleLogout());
	    peerRecognitionButton.setOnAction(e -> {
	    	PeerRecognitionController recognitionController = new PeerRecognitionController();
	    	recognitionController.show();
	    });

	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    userLabel = new Label();
	   

	    //add style class here
	    menuBar.getStyleClass().add("menu-bar");
	    menuBox.getStyleClass().add("menu-box");
	    loginButton.getStyleClass().add("menu-button");
	    logoutButton.getStyleClass().add("menu-button");
	    userLabel.getStyleClass().add("user-label");
	    
	    menuBox.getChildren().addAll(loginButton, logoutButton, peerRecognitionButton, spacer, userLabel);

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
		LoginController loginController = new LoginController(this);
		loginController.showLoginWindow();
	}

	/**
	 * Role-based page loading after login.
	 */
	public void handleLogin(User user) {
		loginButton.setDisable(true);
		logoutButton.setDisable(false);
		peerRecognitionButton.setVisible(true);
		
		username = user.getUsername();
		role = user.getRole().toLowerCase();
		
		userLabel.setText("Welcome, " + username + " (" + role + ")");
		
		
		switch (role) {
		case "admin":
			VBox adminLayout = new VBox(10);
			
			//Styles Idea Submission header
			Label ideaSubmissionLabel = new Label("Idea Submission");
			ideaSubmissionLabel.getStyleClass().add("panel-title");
			ideaSubmissionLabel.setMaxWidth(Double.MAX_VALUE);
			ideaSubmissionLabel.setAlignment(Pos.CENTER);
			
			//Wraps header in a styled container
			VBox ideaHeader = new VBox(ideaSubmissionLabel);
			ideaHeader.getStyleClass().add("panel-header");
			
			IdeaController ideaController = new IdeaController(username);
			AdminController adminController = new AdminController();
			
			
			adminLayout.getChildren().addAll(ideaHeader, ideaController.getView(), adminController.getView());
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
		peerRecognitionButton.setVisible(false);
		setupWelcomePage();
	}
	
	public BorderPane getView() {
		return root;
	}
}