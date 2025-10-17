package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.User;
import util.PasswordUtils;

public class LoginController {
	private Stage loginStage;
	private TextField usernameField;
	private PasswordField passwordField;
	private Label statusLabel;
	private MainController mainController;

	public LoginController(MainController mainController) {
		this.mainController = mainController;
	}

	public void showLoginWindow() {
		loginStage = new Stage();
		loginStage.initModality(Modality.APPLICATION_MODAL);
		loginStage.setTitle("User Login");
		
		//for consistent label width
		double labelWidth = 90;
		
		//labels
		Label usernameLabel = new Label("Username");
		usernameLabel.setPrefWidth(labelWidth);
		Label passwordLabel = new Label("Password");
		passwordLabel.setPrefWidth(labelWidth);
		
		
		//Input Fields
		usernameField = new TextField();
		usernameField.setPromptText("Username");
		usernameField.getStyleClass().add("login-field");
		
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.getStyleClass().add("login-field");
		
		HBox usernameBox = new HBox(10, usernameLabel, usernameField);
		HBox passwordBox = new HBox(10, passwordLabel, passwordField);
		usernameBox.setAlignment(Pos.CENTER_LEFT);
		passwordBox.setAlignment(Pos.CENTER_LEFT);
		
		HBox.setHgrow(usernameField, Priority.ALWAYS);
		HBox.setHgrow(passwordField, Priority.ALWAYS);
		
		// Status Label
		statusLabel = new Label();
		statusLabel.getStyleClass().add("status-label");
		//VBox.setMargin(statusLabel, new Insets(0,0,5,0)); //less space below label
		
		
		//Buttons 
		Button loginButton = new Button("Login");
		loginButton.getStyleClass().add("login-button");
		
		Button cancelButton = new Button("Cancel");
		cancelButton.getStyleClass().add("login-button");
		
		Button helpButton = new Button("Help");
		helpButton.getStyleClass().add("login-button");
		
		//Sets consistent width for all buttons
		loginButton.setPrefWidth(85);
		cancelButton.setPrefWidth(85);
		helpButton.setPrefWidth(85);
		
	
		loginButton.setOnAction(e -> {
			User user = authenticate(usernameField.getText().trim(), passwordField.getText().trim());
			if (user != null) {
				mainController.handleLogin(user);
				loginStage.close();
			} else {
				clearFields(usernameField, passwordField);
				statusLabel.setText("Invalid Credentials.");
			}
		});
		
		cancelButton.setOnAction(e ->loginStage.close());
		helpButton.setOnAction(e -> statusLabel.setText("Enter Username and Password to login."));
		
		Region leftSpacer = new Region();
		leftSpacer.setPrefWidth(labelWidth);
		
		HBox buttonBox = new HBox(15, leftSpacer, loginButton, cancelButton, helpButton);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setPadding(new Insets(20,0,50,0));
		
		//---- Layout ----
		VBox layout = new VBox(14, statusLabel, usernameBox, passwordBox, buttonBox);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20, 20, 15, 20));
		layout.getStyleClass().add("login-window");
		
		
		Scene scene = new Scene(layout, 400, 250);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		
		loginStage.setScene(scene);
		loginStage.showAndWait();
	}


	private User authenticate(String username, String password) {
		try{
			Connection conn = Database.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String storedHashPswd = rs.getString("password_hash");
				if(PasswordUtils.checkPassword(password, storedHashPswd)) {
					return new User(rs.getInt("id"), rs.getString("username"),rs.getString("role"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void clearFields(TextField usernameField, PasswordField passwordField) {
		usernameField.clear();
		passwordField.clear();
	}
}
