package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
		loginStage.setTitle("Login");
		
		usernameField = new TextField();
		usernameField.setPromptText("Username");
		
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		
		statusLabel = new Label();
		
		Button okButton = new Button("OK");
		Button cancelButton = new Button("Cancel");
		Button helpButton = new Button("Help");
		
		VBox layout = new VBox(10, new Label("Username"), usernameField, new Label("Password"), passwordField, new HBox(10, okButton, cancelButton, helpButton), statusLabel);
		
		okButton.setOnAction(e -> {
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
		
		loginStage.setScene(new Scene(layout, 300, 200));
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
