public class Tracker {
    private final String lessonID;
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

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}