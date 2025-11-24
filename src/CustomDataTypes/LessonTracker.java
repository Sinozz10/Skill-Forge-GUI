package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Comparator;

public class LessonTracker extends GeneralTracker {
    @Expose
    private ArrayList<Attempt> attempts = new ArrayList<>();

    public LessonTracker(Lesson lesson) {
        super(lesson.getLessonID());
    }

    public LessonTracker(Lesson lesson, boolean state) {
        super(lesson.getLessonID(), state);
    }

    public LessonTracker(Lesson lesson, ArrayList<Attempt> attempts, boolean state) {
        super(lesson.getLessonID(), state);
        this.attempts.addAll(attempts);
    }


    public int getNumberOfAttempts() {
        return attempts.size();
    }

    public Attempt getHighScore() {
        return attempts.stream().max(Comparator.comparingDouble(Attempt::getScore)).orElse(null);
    }

    public void addAttempt(Attempt attempt) {
        attempts.add(attempt);
    }

    public ArrayList<Attempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(ArrayList<Attempt> attempts) {
        this.attempts = attempts;
    }
}