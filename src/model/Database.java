package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
	private static final String URL = "jdbc:mysql://localhost:3306/IdeaSubmissionDB";
	private static final String USER = "root";
	private static final String PASSWORD = "Saanvi2015";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	//inserts recognition record
	public static void insertPeerRecognition(PeerRecognition pr) throws Exception {
		String insertStmt = "INSERT INTO peer_recognition +"
				+ "(recognized_user_id, recognizer_user_id, recognized_username,+"
				+ "recognizer_name, recognition_text, mindset, recognition_date) +"
				+ "VALUES (?,?,?,?,?,?,?,?)";
		
		try (Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(insertStmt)){
			stmt.setInt(1, pr.getRecognizedUserId());
			stmt.setInt(2, pr.getRecognizerUserId());
			stmt.setString(3, pr.getRecognizedUsername());
			stmt.setString(4, pr.getRecognizerName());
			stmt.setString(5, pr.getProductLine());
			stmt.setString(6, pr.getMindset());
			stmt.setDate(7, java.sql.Date.valueOf(pr.getRecognitionDate()));
			
			stmt.executeUpdate();
		}
	}
}
