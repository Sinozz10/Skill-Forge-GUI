import java.util.ArrayList;
import java.util.Date;

public class Progress {
    private final String courseID;
    private final String studentID;
    private Date completionDate;
    private final ArrayList<Tracker> trackers = new ArrayList<>();
    private boolean updateFlag = false;

    public Progress(Course course, String studentID) {
        this.courseID = course.getID();
        this.studentID = studentID;
        for (Chapter chapter: course.getChapters()){
            for (Lesson lesson: chapter.getLessons()){
                trackers.add(new Tracker(lesson));
            }
        }
    }

    public Progress(Course course, String studentID, boolean updateFlag) {
        this(course, studentID);
        if (updateFlag){
            updateTrackers(course);
            updateFlag = false;
        }
    }

    private Tracker findTracker(Lesson lesson){
        for(Tracker tracker: trackers){
            if (tracker.getLessonID().equals(lesson.getLessonID())){
                return tracker;
            }
        }
        return null;
    }

    public void completeLesson(Lesson lesson) {
        Tracker tracker = findTracker(lesson);
        if (tracker != null){
            tracker.setState(true);
        }else {
            throw new IllegalArgumentException("Lesson not found in tracker");
        }
    }

    public void unCompleteLesson(Lesson lesson) {
        Tracker tracker = findTracker(lesson);
        if (tracker != null){
            tracker.setState(false);
        }else {
            throw new IllegalArgumentException("Uncompleted lesson");
        }
    }

    public ArrayList<Tracker> getTrackers(){
        return trackers;
    }

    public void updateTrackers(Course course){
        ArrayList<String> completed = new ArrayList<>();
        for (Tracker tracker: trackers){
            if (tracker.getState()){
                completed.add(tracker.getLessonID());
            }
        }

        trackers.clear();
        for (Chapter chapter: course.getChapters()){
            for (Lesson lesson: chapter.getLessons()){
                if (completed.contains(lesson.getLessonID())){
                    trackers.add(new Tracker(lesson, true));
                }else {
                    trackers.add(new Tracker(lesson));
                }
            }
        }
    }

    public String getCourseID() {
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

    public boolean isUpdateFlag() {
        return updateFlag;}

    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag = updateFlag;
    }
}
