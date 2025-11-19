package CustomDataTypes;

import java.util.ArrayList;
import java.util.Date;

public class Progress {
    private final String courseID;
    private final String studentID;
    private Date completionDate;
    private final ArrayList<Tracker> trackers = new ArrayList<>();

    public Progress(Course course, String studentID) {
        this.courseID = course.getID();
        this.studentID = studentID;
        for (Chapter chapter: course.getChapters()){
            for (Lesson lesson: chapter.getLessons()){
                trackers.add(new Tracker(lesson));
            }
        }
    }

    public Tracker getTrackerByID(String lessonID){
        for(Tracker tracker: trackers){
            if (tracker.getLessonID().equals(lessonID)){
                return tracker;
            }
        }
        return null;
    }

    public Double getCompletionPercentage(){
        int total = 0;
        int complete = 0;
        for(Tracker tracker: trackers){
            if (tracker.isComplete()){
                complete++;
            }
            total++;
        }

        return ((double) (complete/total))*100;
    }

    public void completeLesson(String lessonID) {
        Tracker tracker = getTrackerByID(lessonID);
        if (tracker != null){
            tracker.setComplete(true);
        }else {
            throw new IllegalArgumentException("CustomDataTypes.Lesson not found in tracker");
        }
    }

    public void unCompleteLesson(String lessonID) {
        Tracker tracker = getTrackerByID(lessonID);
        if (tracker != null){
            tracker.setComplete(false);
        }else {
            throw new IllegalArgumentException("Uncompleted lesson");
        }
    }

    public ArrayList<Tracker> getTrackers(){
        return trackers;
    }

    public void updateTrackers(Course course){
        if (!courseID.equals(course.getID())){
            throw new IllegalArgumentException("Incorrect CustomDataTypes.Course");
        }

        ArrayList<String> completed = new ArrayList<>();
        for (Tracker tracker: trackers){
            if (tracker.isComplete()){
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
}
