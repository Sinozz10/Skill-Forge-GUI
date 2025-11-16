import java.util.ArrayList;

public class Instructor extends User{
    private ArrayList<Integer> courseIDs = new ArrayList<>();

    public Instructor(String username, String role, String uID, String email, String hashedPassword, CourseDB database) {
        super(username, role, uID, email, hashedPassword, database);
    }

    public ArrayList<Integer> getCourseIDs(){
        return courseIDs;
    }

    public void setCourseIDs(ArrayList<Integer> courseIDs){
        this.courseIDs = courseIDs;
    }

    public void addCourse(int courseID){
        courseIDs.add(courseID);
    }

    public void removeCourse(int courseID){
        courseIDs.remove(courseID);
    }
}
