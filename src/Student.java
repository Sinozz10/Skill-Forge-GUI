import java.util.ArrayList;
import java.util.Objects;

public class Student extends User{
    private final ArrayList<Progress> progressTrackers = new ArrayList<>();

    public Student(String uID, String role, String username, String email, String hashedPassword) {
        super(uID, role, username, email, hashedPassword);
    }

    @Override
    public void addCourse(Course course){
        courseIDs.add(course.getID());
        progressTrackers.add(new Progress(course, userID));
    }

    @Override
    public void removeCourse(Course course){
        courseIDs.remove(course.getID());
        for(Progress prog: progressTrackers){
           if (course.getID().equals(prog.getCourseID())){
               progressTrackers.remove(prog);
               break;
           }
        }
    }
}
