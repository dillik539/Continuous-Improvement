import javafx.application.Application;
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
		
		loginButton.setOnAction(e -> {
			String username = userNameField.getText();
			String password = passwordField.getText();
			
			if(controller.authenticateUser(username, password)) {
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
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}