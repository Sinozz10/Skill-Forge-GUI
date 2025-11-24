package CustomDataTypes;

public class Admin extends User {
    public Admin(String uID, String role, String username, String email, String hashedPassword) {
        super(uID, role, username, email, hashedPassword);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword +
                '}';
    }
}
