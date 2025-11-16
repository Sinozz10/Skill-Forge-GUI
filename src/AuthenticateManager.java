public class AuthenticateManager {

    private JsonDatabaseManager database;

    public AuthenticateManager(JsonDatabaseManager database) {
        this.database = database;
    }

    public User signup(String username, String email, String password, String role) {
        // Check if fields are empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        // Check if email already exists
        if (database.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered!");
        }

        //Hash the password
        String hashedPassword = PasswordHashing.hashPassword(password);

        // Generate unique ID
        String userId = role.equalsIgnoreCase("student") ?
                "S" + System.currentTimeMillis() :
                "I" + System.currentTimeMillis();


        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, username, email, hashedPassword, database);
        } else {
            newUser = new Instructor(userId, username, email, hashedPassword, database);
        }

        database.saveUser(newUser);

        return newUser;
    }

    public User login(String email, String password) {
        // 1. Check if fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            return null;
        }

        // 2. Get user from database
        User user = database.getUserByEmail(email);
        if (user == null) {
            return null; // User not found
        }

        // 3. Check if password matches
        String hashedPassword = PasswordHashing.hashPassword(password);
        if (hashedPassword.equals(user.getHashedPassword())) {
            return user; // Login successful!
        }

        return null; // Wrong password
    }
}
}
