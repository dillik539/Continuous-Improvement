import java.sql.*;

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
	protected boolean authenticateUser(String username, String password) {
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

}
