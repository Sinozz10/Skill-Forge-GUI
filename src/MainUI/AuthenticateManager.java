package MainUI;

import CustomDataTypes.Admin;
import DataManagment.*;

import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomDataTypes.User;

public class AuthenticateManager {
    protected final UserDatabaseManager database = UserDatabaseManager.getDatabaseInstance();
    private final GenerationID idGenerator;

    public AuthenticateManager() {
        this.idGenerator = new GenerationID();
    }

    public User signup(String username, String email, String password, String role) {
        // Check if fields are empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        // Check if email already exists
        if (database.getRecordByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered!");
        }

        //Hash the password
        String hashedPassword = PasswordHashing.hashPassword(password);

        // Generate unique ID
        String userId = idGenerator.generateUserID(role);

        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, role, username, email, hashedPassword);
        } else  if (role.equalsIgnoreCase("instructor")) {
            newUser = new Instructor(userId, role, username, email, hashedPassword);
        }else {
            newUser = new Admin(userId, role, username, email, hashedPassword);
        }

        database.addRecord(newUser);
        database.saveToFile();
        return newUser;
    }

    public User login(String username, String password) {
        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        // Get user from database
        User user = database.getRecordByUsername(username);
        if (user == null) {
            return null; // CustomDataTypes.User not found
        }

        // Check if password matches
        if (PasswordHashing.verifyPassword(password, user.getHashedPassword())) {
            return user;
        }
        return null; // Wrong password
    }


}
