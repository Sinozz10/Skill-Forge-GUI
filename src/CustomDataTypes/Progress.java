package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Date;

public class Progress {
    @Expose
    private final String courseID;
    @Expose
    private final String studentID;
    @Expose
    private Date completionDate;
    @Expose
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
        if(trackers.isEmpty()){
            return 0.0;
        }

        long complete = trackers.stream()
                .filter(Tracker::isComplete)
                .count();
        if (trackers.isEmpty()){
            return 0.0;
        }
        return (complete * 100.0) / trackers.size();
    }

    public boolean isCourseComplete() {
        if (trackers.isEmpty()) return false;
        return trackers.stream().allMatch(Tracker::isComplete);
    }

    public void completeLesson(String lessonID) {
        Tracker tracker = getTrackerByID(lessonID);
        if (tracker != null){
            tracker.setComplete(true);
        }else {
            throw new IllegalArgumentException("Lesson not found in tracker");
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
            throw new IllegalArgumentException("Incorrect Course");
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
