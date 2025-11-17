import java.util.ArrayList;

public class User implements Record{
    //Attributes
    protected String username;
    protected String role; //identifier
    protected String userID;
    protected String email;
    protected String hashedPassword;
    protected ArrayList<String> courseIDs = new ArrayList<>();

    //Constructor
    public User(String uID, String role, String username, String email, String hashedPassword) {
        this.username = username;
        this.role = role;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userID = uID;
    }


    //Getters and Setters + toString
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userID + '\'' +
                ",role='" + role + '\'' +
                ",username='" + username + '\'' +
                ",email='" + email + '\'' +
                '}';
    }

    @Override
    public String getID() {
        return userID;
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

    public ArrayList<String> getCourseIDs(){
        return courseIDs;
    }

    public void setCourses(ArrayList<String> courseIDs){
        this.courseIDs = courseIDs;
    }

    public void addCourse(Course course){
        courseIDs.add(course.getID());
    }

    public void removeCourse(Course course){
        courseIDs.remove(course.getID());
    }
}
