package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
		addUserStage.setTitle("Add New User");
		
		//For consistent Label width
		double labelWidth = 90;
		
		Label usernameLabel = new Label("Username");
		usernameLabel.setPrefWidth(labelWidth);
		Label passwordLabel = new Label("Password");
		passwordLabel.setPrefWidth(labelWidth);
		Label roleLabel = new Label("User Role");
		roleLabel.setPrefWidth(labelWidth);

		usernameField = new TextField();
		usernameField.setPromptText("Enter username");
		
		passwordField = new PasswordField();
		passwordField.setPromptText("Enter password");
		
		roleComboBox = new ComboBox<>();
		roleComboBox.getItems().addAll("User", "Admin", "Team Lead", "Supervisor", "Manager");
		roleComboBox.setPromptText("Select role");
		roleComboBox.setMaxWidth(Double.MAX_VALUE);
		
		//Aligns labels and fields horizontally
		HBox usernameBox = new HBox(10, usernameLabel, usernameField);
		HBox passwordBox = new HBox(10, passwordLabel, passwordField);
		HBox roleBox = new HBox(10, roleLabel,roleComboBox);
		
		usernameBox.setAlignment(Pos.CENTER_LEFT);
		passwordBox.setAlignment(Pos.CENTER_LEFT);
		roleBox.setAlignment(Pos.CENTER_LEFT);
		
		HBox.setHgrow(usernameField, Priority.ALWAYS);
		HBox.setHgrow(passwordField, Priority.ALWAYS);
		HBox.setHgrow(roleComboBox, Priority.ALWAYS);
		
		
		Button addButton = new Button("Add");
		Button cancelButton = new Button("Cancel");
		
		addButton.setPrefWidth(140);
		cancelButton.setPrefWidth(140);
		addButton.getStyleClass().add("action-button");
		cancelButton.getStyleClass().add("action-button");
		
		//Add this spacer before buttons to align them with text fields
		Region leftSpacer = new Region();
		leftSpacer.setPrefWidth(labelWidth); //sets the width same as label width
		
		HBox buttonBox = new HBox(15, leftSpacer, addButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER_LEFT); //Aligns with the text fields
		buttonBox.setPadding(new Insets(15, 0,50,0)); //increases spacing between comboxbox and buttons
		
		statusLabel = new Label();
		statusLabel.getStyleClass().add("status-label");

		addButton.setOnAction(e -> addUser());
		cancelButton.setOnAction(e -> addUserStage.close());

		VBox layout = new VBox(14, statusLabel, usernameBox, passwordBox, roleBox, buttonBox);
		layout.getStyleClass().add("add-user-window");
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20, 20, 15, 20));
		
		//Scene
		Scene scene = new Scene(layout, 420, 300);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

		addUserStage.setScene(scene);
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
