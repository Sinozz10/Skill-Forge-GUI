package CustomDataTypes;

import com.google.gson.annotations.Expose;

public class Tracker {
    @Expose
    private final String lessonID;
    @Expose
    private boolean state;

    public Tracker(Lesson lesson) {
        this.lessonID = lesson.getLessonID();
        this.state = false;
    }

    public Tracker(Lesson lesson, boolean state) {
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