public abstract class User {
    //Attributes
    protected String username;
    protected String role; //identifier
    protected String uID;
    protected String email;
    protected String hashedPassword;
    protected CourseDB database;

    //Constructor
    public User(String username, String role, String uID, String email, String hashedPassword, CourseDB database) {
        this.username = username;
        this.role = role;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.uID = uID;
        this.database = database;
    }


    //Getters and Setters + toString
    @Override
    public String toString() {
        return "User{" +
                "userId='" + uID + '\'' +
                ",role='" + role + '\'' +
                ",username='" + username + '\'' +
                ",email='" + email + '\'' +
                '}';
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
