import java.util.ArrayList;
import java.util.Date;

public class Progress {
    private int courseID;
    private String studentID;
    private Date completionDate;
    private ArrayList<Integer> lessonIDs = new ArrayList<>();
    private ArrayList<Boolean> tracker = new ArrayList<>();

    public Progress(int courseID, String  studentID, CourseDB database) {
        this.courseID = courseID;
        this.studentID = studentID;
        for (Lesson lesson: database.fetchLessonsByCourse(courseID)){
            lessonIDs.add(lesson.getLessonID());
            tracker.add(false);
        }

    }

    public void completeLesson(int lessonID) {
        tracker.set(lessonIDs.indexOf(lessonID), true);
    }

    public void unCompleteLesson(int lessonID) {
        tracker.set(lessonIDs.indexOf(lessonID), false);
    }

    public int getCourseID() {
        return courseID;
    }

    public String getStudentID() {
        return studentID;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
}
