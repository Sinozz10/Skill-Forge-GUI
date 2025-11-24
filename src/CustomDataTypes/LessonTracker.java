package CustomDataTypes;

import com.google.gson.annotations.*;


public class LessonTracker {
    @Expose
    private final String lessonID;
    @Expose
    private boolean state;

    public LessonTracker(Lesson lesson) {
        this.lessonID = lesson.getLessonID();
        this.state = false;
    }

    public LessonTracker(Lesson lesson, boolean state) {
        this.lessonID = lesson.getLessonID();
        this.state = state;
    }

    public String getLessonID() {
        return lessonID;
    }

    public boolean isComplete() {
        return state;
    }

    public void setComplete(boolean state) {
        this.state = state;
    }
}