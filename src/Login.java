import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class Login extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		//UI elements
		Label userName = new Label("Username:");
		TextField userNameField = new TextField();
		
		Label passwordLabel = new Label("Password:");
		PasswordField passwordField = new PasswordField();
		
		Button loginButton = new Button("Login");
		Label messageLabel = new Label();
		
		loginButton.setOnAction(e -> {
			String username = userNameField.getText();
			String password = passwordField.getText();
			
			if(authenticateUser(username, password)) {
				messageLabel.setText("Login Successful!");
			}else {
				messageLabel.setText("Login Failed! Check your username and/or password.");
			}
		});
		
		//layout
		
		VBox layout = new VBox(10); //sets each element vertically with spacing of 10 pixels.
		layout.getChildren().addAll(userName, userNameField, passwordLabel, passwordField, loginButton, messageLabel);
		
		//establish scene
		
		Scene scene = new Scene(layout, 300, 200);
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/*
	 * This method checks if username and password exist in the database.
	 * If exist, it returns True, if not, returns false.
	 */
	private boolean authenticateUser(String username, String password) {
		String databaseURL = "jdbc:mysql://localhost:3306/javafx_login";
		String databaseUser = "root"; //replace with your mysql database user
		String databasePassword = "Saanvi2015"; //replace with your mysql database password
		String sqlQuery = "SELECT * FROM users WHERE username = ? and password = ?";
		try {
			Connection conn = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
			PreparedStatement statement = conn.prepareStatement(sqlQuery);
			statement.setString(1, username);
			statement.setString(2, password);
			
			ResultSet rs = statement.executeQuery();
			return rs.next();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}