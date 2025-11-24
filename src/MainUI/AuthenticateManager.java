package MainUI;

import CustomDataTypes.Admin;
import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomDataTypes.User;
import DataManagment.GenerationID;
import DataManagment.UserDatabaseManager;

public class AuthenticateManager {
    protected final UserDatabaseManager database = UserDatabaseManager.getDatabaseInstance();
    private final GenerationID idGenerator;

    public AuthenticateManager() {
        this.idGenerator = new GenerationID();
    }

    public User signup(String username, String email, String password, String role) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }
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
        } else if (role.equalsIgnoreCase("instructor")) {
            newUser = new Instructor(userId, role, username, email, hashedPassword);
        } else {
            newUser = new Admin(userId, role, username, email, hashedPassword); //Zawedt User Admin
        }

        database.addRecord(newUser);
        database.saveToFile();
        return newUser;
    }

    public User login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }
        User user = database.getRecordByUsername(username);
        if (user == null) {
            return null;
        }

        // Check if password matches
        if (PasswordHashing.verifyPassword(password, user.getHashedPassword())) {
            return user;
        }
        return null; // Wrong password
    }
}
