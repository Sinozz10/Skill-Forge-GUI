
public class AuthenticateManager {
    private final UserDatabaseManager database;

    public AuthenticateManager(UserDatabaseManager database) {
        this.database = database;
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
        String userId = generatedID(role);

        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, role, username, email, hashedPassword);
        } else {
            newUser = new Instructor(userId, role, username, email, hashedPassword);
        }

        database.saveToFile();

        return newUser;
    }

    public User login(String email, String password) {
        // 1. Check if fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            return null;
        }

        // 2. Get user from database
        User user = database.getRecordByEmail(email);
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

    private String generatedID(String role) {
        String prefix = role.equalsIgnoreCase("student") ? "S" : "I";
        int highest = getHighestID(role);// Count existing users of this role
        return String.format("%s%04d", prefix, highest + 1);
    }

     private int getHighestID(String role) {
        int highest = 0;
        for (User user : database.getRecords()) {
            if (user.getRole().equalsIgnoreCase(role)) {
                String userId = user.userID;
                // 3mltaha 3ashan t-Extract number from ID
                try {
                    String numberPart = userId.substring(1); // Remove prefix
                    int idNumber = Integer.parseInt(numberPart);
                    if (idNumber > highest) {
                        highest = idNumber;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return highest;
    }
}
