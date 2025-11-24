package CustomDataTypes;

import com.google.gson.annotations.*;

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
    private final ArrayList<LessonTracker> trackers = new ArrayList<>();

    public Progress(Course course, String studentID) {
        this.courseID = course.getID();
        this.studentID = studentID;
        for (Chapter chapter: course.getChapters()){
            for (Lesson lesson: chapter.getLessons()){
                trackers.add(new LessonTracker(lesson));
            }
        }
    }

    public LessonTracker getTrackerByID(String lessonID){
        for(LessonTracker tracker: trackers){
            if (tracker.getLessonID().equals(lessonID)){
                return tracker;
            }
        }
        return null;
    }

    public Double getCompletionPercentage(){
        long complete = trackers.stream()
                .filter(LessonTracker::isComplete)
                .count();
        if (trackers.isEmpty()){
            return 0.0;
        }
        return rounder( (complete * 100.0)/trackers.size() );
    }

    public boolean isCourseComplete() {
        if (trackers.isEmpty()) return false;
        return trackers.stream().allMatch(LessonTracker::isComplete);
    }

    public void completeLesson(String lessonID) {
        LessonTracker tracker = getTrackerByID(lessonID);
        if (tracker != null){
            tracker.setComplete(true);
        }else {
            throw new IllegalArgumentException("Lesson not found in tracker");
        }
    }

    public void unCompleteLesson(String lessonID) {
        LessonTracker tracker = getTrackerByID(lessonID);
        if (tracker != null){
            tracker.setComplete(false);
        }else {
            throw new IllegalArgumentException("Uncompleted lesson");
        }
    }

    public ArrayList<LessonTracker> getTrackers(){
        return trackers;
    }

    public void updateTrackers(Course course){
        if (!courseID.equals(course.getID())){
            throw new IllegalArgumentException("Incorrect Course");
        }

        ArrayList<String> completed = new ArrayList<>();
        for (LessonTracker tracker: trackers){
            if (tracker.isComplete()){
                completed.add(tracker.getLessonID());
            }
        }

        trackers.clear();
        for (Chapter chapter: course.getChapters()){
            for (Lesson lesson: chapter.getLessons()){
                if (completed.contains(lesson.getLessonID())){
                    trackers.add(new LessonTracker(lesson, true));
                }else {
                    trackers.add(new LessonTracker(lesson));
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

    private static double rounder(double mark){
        int decimalPlaces = 2;
        // Round to 2 decimal places
        double roundedNumber = Math.round(mark * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);

        return roundedNumber;
    }
}
