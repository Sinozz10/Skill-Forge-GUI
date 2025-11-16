import java.util.ArrayList;

public class Student extends User{
    private ArrayList<Progress> progressTrackers = new ArrayList<>();

    public Student(String username, String role, String uID, String email, String hashedPassword) {
        super(username, role, uID, email, hashedPassword);
    }

    @Override
    public void addCourse(Course course){
        courses.add(course);
        progressTrackers.add(new Progress(course, uID));
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
