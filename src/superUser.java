public abstract class superUser {
    //Attributes
    private String username;
    private String password;
    private String role; //identifier
    private String uID;
    private String email;
    private String hashedPassword;

    //Constructor
    public superUser(String username, String password, String role, String uID, String email, String hashedPassword) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.uID = uID;
    }


    //Getters and Setters + toString


    @Override
    public String toString() {
        return "User{" +
                "userId='" + uID + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
