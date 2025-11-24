package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class Progress {
    @Expose
    private final String courseID;
    @Expose
    private final String studentID;
    @Expose
    private Date completionDate;
    @Expose
    private ArrayList<LessonTracker> trackers = new ArrayList<>();

    public Progress(Course course, String studentID) {
        this.courseID = course.getID();
        this.studentID = studentID;
        for (Chapter chapter : course.getChapters()) {
            for (Lesson lesson : chapter.getLessons()) {
                trackers.add(new LessonTracker(lesson));
            }
        }
    }

    public LessonTracker getTrackerByLessonID(String lessonID) {
        for (LessonTracker tracker : trackers) {
            if (tracker.getID().equals(lessonID)) {
                return tracker;
            }
        }
        return null;
    }

    public Double getCompletionPercentage() {
        long complete = trackers.stream()
                .filter(LessonTracker::isTrue)
                .count();
        if (trackers.isEmpty()) {
            return 0.0;
        }
        return rounder((complete * 100.0) / trackers.size());
    }

    public boolean isCourseComplete() {
        if (trackers.isEmpty()) return false;
        return trackers.stream().allMatch(LessonTracker::isTrue);
    }

    public void completeLesson(String lessonID) {
        LessonTracker tracker = getTrackerByLessonID(lessonID);
        if (tracker != null) {
            tracker.setState(true);
        } else {
            throw new IllegalArgumentException("Lesson not found in tracker");
        }
    }

    public void unCompleteLesson(String lessonID) {
        LessonTracker tracker = getTrackerByLessonID(lessonID);
        if (tracker != null) {
            tracker.setState(false);
        } else {
            throw new IllegalArgumentException("Lesson not found in tracker");
        }
    }

    public ArrayList<LessonTracker> getTrackers() {
        return trackers;
    }

    public void updateTrackers(Course course) {
        if (!courseID.equals(course.getID())) {
            throw new IllegalArgumentException("Incorrect Course");
        }

        ArrayList<LessonTracker> newTrackers = new ArrayList<>();

        for (Chapter chapter : course.getChapters()) {
            for (Lesson lesson : chapter.getLessons()) {
                Optional<LessonTracker> o = trackers.stream().filter(tracker -> tracker.getID().equals(lesson.getLessonID())).findFirst();

                if (o.isPresent()) {
                    LessonTracker t = o.get();
                    newTrackers.add(new LessonTracker(lesson,t.getAttempts(), t.isTrue()));
                } else {
                    newTrackers.add(new LessonTracker(lesson));
                }
            }
        }

        trackers.clear();
        trackers.addAll(newTrackers);

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

    private static double rounder(double mark) {
        return Math.round(mark * Math.pow(10, 2)) / Math.pow(10, 2);
    }
}
