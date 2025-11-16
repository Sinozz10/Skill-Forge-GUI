import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Student extends User{
    private ArrayList<Integer> courseIDs = new ArrayList<>();
    private ArrayList<Progress> progressTrackers = new ArrayList<>();

    public Student(String username, String role, String uID, String email, String hashedPassword, CourseDB database) {
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
        progressTrackers.add(new Progress(courseID, uID, database));
    }

    public void removeCourse(int courseID){
        courseIDs.remove(courseID);
        for(Progress prog: progressTrackers){
           if (prog.getCourseID() == courseID){
               progressTrackers.remove(prog);
               break;
           }
        }
    }
}
