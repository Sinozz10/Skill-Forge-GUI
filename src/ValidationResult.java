import javax.swing.*;
import java.util.regex.Pattern;

public class ValidationResult{

    // Email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z+_.-]+@[A-Za-z.-]+\\.[A-Za-z]$"
    );

    // Username Validation
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[A-Za-z0-9][A-Za-z0-9 -]{1,48}[A-Za-z0-9]$"
    );

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String trimmed = username.trim();
        return trimmed.length() >= 3 &&
                trimmed.length() <= 50 &&
                USERNAME_PATTERN.matcher(trimmed).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static String getUsernameError() {
        return "Username must be 3-50 characters, letters/spaces/hyphens only.";
    }

    public static String getEmailError() {
        return "Please enter a valid email address (e.g., user@example.com).";
    }

    public static String getPasswordError() {
        return "Password must be at least 6 characters long.";
    }

}