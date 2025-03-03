import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

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
			System.err.println("Error connecting to database. " + e.getMessage());
		}
	}
	protected boolean authenticateUser(String username, String password) {
		String sqlQuery = "SELECT password FROM users WHERE username = ?";
		try {
			Connection conn = connectDatabase();
			PreparedStatement statement = conn.prepareStatement(sqlQuery);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				String storedHashedPwd = rs.getString("password");
				return checkPassword(password, storedHashedPwd);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
