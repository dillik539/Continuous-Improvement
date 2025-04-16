package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
	public static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(10));
	}

	public static boolean checkPassword(String password, String hashed) {
		return BCrypt.checkpw(password, hashed);
	}

	public static boolean validatePassword(String userEnteredPswd, String storedPswd) {
		return userEnteredPswd.equals(storedPswd);
	}
}
