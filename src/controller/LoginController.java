package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

public class LoginController {
	private Stage loginStage;
	private TextField usernameField;
	private PasswordField passwordField;
	private Label errorLabel;
	private MainController mainController;

	public LoginController(MainController mainController) {
		this.mainController = mainController;
		loginStage = new Stage();
		loginStage.initModality(Modality.APPLICATION_MODAL);
		loginStage.setTitle("Login");

		VBox layout = new VBox(10);
		usernameField = new TextField();
		passwordField = new PasswordField();
		errorLabel = new Label();

		HBox okCancelLayout = new HBox(10);
		Button okButton = new Button("OK");
		Button cancelButton = new Button("Cancel");
		okCancelLayout.getChildren().addAll(okButton, cancelButton);

		okButton.setOnAction(e -> handleLogin());
		cancelButton.setOnAction(e -> loginStage.close());

		layout.getChildren().addAll(new Label("Username"), usernameField, new Label("Password"), passwordField,
				errorLabel, okCancelLayout);

		loginStage.setScene(new Scene(layout, 300, 200));
	}

	public void showLoginWindow() {
		loginStage.showAndWait();
	}

	private void handleLogin() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();

		if (username.isEmpty() || password.isEmpty()) {
			errorLabel.setText("Fields cannot be empty.");
			return;
		}

		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn
					.prepareStatement("SELECT username FROM users WHERE username = ? AND password_hash = ?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			// TO DO: replace validatePassword() with checkPswd after hashing issue is sort
			// out.
			if (rs.next()) {
				loginStage.close();
				mainController.handleLogin(username);
			} else {
				errorLabel.setText("Invalid credentials.");
				clearFields();

			}
		} catch (Exception e) {
			errorLabel.setText("Database error.");
			e.printStackTrace();
		}
	}

	private void clearFields() {
		usernameField.clear();
		passwordField.clear();
	}
}
