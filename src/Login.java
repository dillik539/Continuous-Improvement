import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
			String message = "Welcome to the JavaFX login app!\nLogin button is not yet functional.";
			
			messageLabel.setText(message);
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