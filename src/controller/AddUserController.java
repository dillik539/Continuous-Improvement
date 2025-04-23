package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Database;
import util.PasswordUtils;

public class AddUserController {
	private Stage addUserStage;
	private TextField usernameField;
	private PasswordField passwordField;
	private ComboBox<String> roleComboBox;
	private Label statusLabel;

	public AddUserController() {
		addUserStage = new Stage();
		addUserStage.setTitle("Add User");

		usernameField = new TextField();
		passwordField = new PasswordField();
		roleComboBox = new ComboBox<>();
		roleComboBox.getItems().addAll("User", "Admin", "Team Lead", "Supervisor", "Manager");

		Button addButton = new Button("Add");
		Button cancelButton = new Button("Cancel");
		statusLabel = new Label();

		addButton.setOnAction(e -> addUser());
		cancelButton.setOnAction(e -> addUserStage.close());

		VBox layout = new VBox(10, new Label("Username"), usernameField, new Label("Password"), passwordField,
				new Label("Role"), roleComboBox, addButton, cancelButton, statusLabel);

		addUserStage.setScene(new Scene(layout, 300, 250));
	}

	private void addUser() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();
		String role = roleComboBox.getValue();

		if (username.isEmpty() || password.isEmpty() || role == null) {
			statusLabel.setText("All fields are required.");
			return;
		}

		String hashedPassword = PasswordUtils.hashPassword(password);

		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)");
			stmt.setString(1, username);
			stmt.setString(2, hashedPassword);
			stmt.setString(3, role);
			stmt.executeUpdate();
			statusLabel.setText("User added successfully!");
		} catch (Exception e) {
			statusLabel.setText("Error adding user.");
			e.printStackTrace();
		}
	}

	public void show() {
		addUserStage.show();
	}
}
