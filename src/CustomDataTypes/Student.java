package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Student extends User {
    @Expose
    private final ArrayList<Progress> progressTrackers = new ArrayList<>();

    public Student(String uID, String role, String username, String email, String hashedPassword) {
        super(uID, role, username, email, hashedPassword);
    }

    public ArrayList<Progress> getAllProgressTrackers() {
        return progressTrackers;
    }

    public Progress getProgressTrackerByCourseID(String ID) {
        for (Progress prog : progressTrackers) {
            if (prog.getCourseID().equals(ID)) {
                return prog;
            }
        }
        return null;
    }

    @Override
    public void addCourse(Course course) {
        courseIDs.add(course.getID());
        progressTrackers.add(new Progress(course, userID));
    }

    @Override
    public void removeCourse(Course course) {
        courseIDs.remove(course.getID());
        progressTrackers.removeIf(prog -> course.getID().equals(prog.getCourseID()));
    }
}
