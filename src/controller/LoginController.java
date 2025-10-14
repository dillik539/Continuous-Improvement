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
		
		
		//---- Input Fields ---
		usernameField = new TextField();
		usernameField.setPromptText("Username");
		usernameField.getStyleClass().add("login-field");
		
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.getStyleClass().add("login-field");
		
		//---- Status Label ---
		statusLabel = new Label();
		statusLabel.getStyleClass().add("login-status");
		
		
		//---- Buttons ----
		Button loginButton = new Button("Login");
		loginButton.getStyleClass().add("login-button");
		
		Button cancelButton = new Button("Cancel");
		cancelButton.getStyleClass().add("login-button");
		
		Button helpButton = new Button("Help");
		helpButton.getStyleClass().add("login-button");
		
		//Sets consistent width for all buttons
		loginButton.setPrefWidth(100);
		cancelButton.setPrefWidth(100);
		helpButton.setPrefWidth(100);
		
	
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
		helpButton.setOnAction(e -> statusLabel.setText("Contact admin for credentials."));
		
		HBox buttonBox = new HBox(10, loginButton, cancelButton, helpButton);
		buttonBox.setAlignment(Pos.CENTER);
		
		//---- Layout ----
		VBox layout = new VBox(15, usernameField, passwordField, buttonBox, statusLabel);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20));
		layout.getStyleClass().add("login-window");
		
		
		Scene scene = new Scene(layout, 350, 250);
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
