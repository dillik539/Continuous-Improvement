import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

import javafx.scene.control.Alert;

/**
 * @author Dilli
 * This class control the logic of the program.
 *
 */
public class Controller {	
	/*
	 * This method checks if username and password exist in the database.
	 * If exist, it returns True, if not, returns false.
	 */
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/javafx_login";
	private static final String DATABASE_USER = "root"; //replace with your mysql database user
	private static final String DATABASE_PSWD = "Saanvi2015"; //replace with your mysql database password
	
	private Connection connectDatabase() throws SQLException{
		return DriverManager.getConnection(DATABASE_URL, DATABASE_USER,DATABASE_PSWD);
		
	}
	
	private String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}
	
	private boolean checkPassword(String plainPassword, String hashPassword) {
		return BCrypt.checkpw(plainPassword, hashPassword);
	}
	
	protected void registerUser(String username, String password) {
		String insertQuery = "INSERT INTO users (username, password) VALUES (?,?)";
		String hashedPassword = hashPassword(password);
		try {
			Connection conn = connectDatabase();
			PreparedStatement prestmt = conn.prepareStatement(insertQuery);
			prestmt.setString(1, username);
			prestmt.setString(2, hashedPassword);
			prestmt.executeUpdate();
		}catch(SQLException e) {
			showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to Connect to Database!");
		}
	}
	protected boolean authenticateUser(String username, String password) {
		String sqlQuery = "SELECT password FROM users WHERE username = ?";
		try
			{Connection conn = connectDatabase();
			PreparedStatement statement = conn.prepareStatement(sqlQuery);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				String storedHashedPwd = rs.getString("password");
				if(!checkPassword(password, storedHashedPwd)) {
					showAlert(Alert.AlertType.WARNING, "User Authentication", "Login Failed!");
					return false;
				}else {
					showAlert(Alert.AlertType.INFORMATION, "User Authentication", "Login Successfull!");
					return true;
				}
		
			}else {
				showAlert(Alert.AlertType.WARNING, "No User", "No user found! Please register.");
				return false;
			}
		}catch(SQLException e) {
			showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to Connect to Database!");
			return false;
		}
	}
	
	private void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		
	}

}
