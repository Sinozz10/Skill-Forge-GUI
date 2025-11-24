package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Comparator;

public class QuizLessonTracker extends LessonTracker {
    @Expose
    private ArrayList<Attempt> attempts = new ArrayList<>();

    public QuizLessonTracker(Lesson lesson) {
        super(lesson);
    }

    public QuizLessonTracker(Lesson lesson, boolean state) {
        super(lesson, state);
    }

    public QuizLessonTracker(Lesson lesson, ArrayList<Attempt> attempts, boolean state) {
        super(lesson, state);
        this.attempts = attempts;
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

