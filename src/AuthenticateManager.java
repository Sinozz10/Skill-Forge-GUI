
public class AuthenticateManager {
    private final UserDatabaseManager database;
    private final GenerationID idGenartor;

    public AuthenticateManager(UserDatabaseManager database) {
        this.database = database;
        this.idGenartor = new GenerationID(database,null);
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
        String userId = idGenartor.generateUserID(role);

        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, role, username, email, hashedPassword);
        } else {
            newUser = new Instructor(userId, role, username, email, hashedPassword);
        }

        database.addRecord(newUser);
        database.saveToFile();
        return newUser;
    }

    public User login(String username, String password) {
        // 1. Check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        // 2. Get user from database
        User user = database.getRecordByUsername(username);
        if (user == null) {
            return null; // User not found
        }

        // 3. Check if password matches
        if (PasswordHashing.verifyPassword(password, user.getHashedPassword())) {
            return user;
        }
        return null; // Wrong password
    }


}
