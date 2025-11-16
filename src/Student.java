import java.util.ArrayList;

public class Student extends User{
    private ArrayList<Progress> progressTrackers = new ArrayList<>();

    public Student(String uID, String role, String username, String email, String hashedPassword) {
        super(uID, role, username, email, hashedPassword);
    }

    @Override
    public void addCourse(Course course){
        courses.add(course);
        progressTrackers.add(new Progress(course, userID));
    }

    @Override
    public void removeCourse(Course course){
        courses.remove(course);
        for(Progress prog: progressTrackers){
           if (prog.getCourse() == course){
               progressTrackers.remove(prog);
               break;
           }
        }
    }
}
