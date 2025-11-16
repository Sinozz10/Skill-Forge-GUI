public class Tracker {
    private final Lesson lesson;
    private boolean state;

    public Tracker(Lesson lesson) {
        this.lesson = lesson;
        this.state = false;
    }

    public Tracker(Lesson lesson, boolean state) {
        this.lesson = lesson;
        this.state = state;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
