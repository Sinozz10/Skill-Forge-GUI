public class Instructor extends User{
    public Instructor(String uID, String role, String username, String email, String hashedPassword) {
        super(uID, role, username, email, hashedPassword);
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", courseIDs=" + courseIDs +
                '}';
    }
}
