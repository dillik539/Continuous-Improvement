import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends Application{
	Controller controller = new Controller();

	@Override
	public void start(Stage primaryStage) throws Exception {
		//UI elements
		Label userName = new Label("Username:");
		TextField userNameField = new TextField();
		
		Label passwordLabel = new Label("Password:");
		PasswordField passwordField = new PasswordField();
		
		Button loginButton = new Button("Login");
		Label messageLabel = new Label();
		
		Label registerLabel = new Label("Not registered? Click the button below.");
		Button registerButton = new Button("Register");
		
		loginButton.setOnAction(e -> {
			String username = userNameField.getText();
			String password = passwordField.getText();
			
			if(controller.authenticateUser(username, password)) {
				messageLabel.setText("Login Successful!");
			}else {
				messageLabel.setText("Login Failed! Check your username and/or password.");
			}
			userNameField.clear();
			passwordField.clear();
		});
		
		registerButton.setOnAction(e -> {
			String registerUsername = userNameField.getText();
			String registerPassword = passwordField.getText();
			
			if(!registerUsername.isEmpty() && !registerPassword.isEmpty()) {
				controller.registerUser(registerUsername, registerPassword);
				messageLabel.setText("User Successfully registered!");
			}else {
				messageLabel.setText("Username and Password cannot be empty.");
			}
			userNameField.clear();
			passwordField.clear();
			
		});
		
		//layout
		
		VBox layout = new VBox(10); //sets each element vertically with spacing of 10 pixels.
		layout.getChildren().addAll(userName, userNameField, passwordLabel, passwordField, loginButton, registerLabel, registerButton, messageLabel);
		layout.setAlignment(Pos.CENTER);
		//establish scene
		
		Scene scene = new Scene(layout, 300, 250);
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}