package CustomDataTypes;

public class QuizLessonTracker extends LessonTracker{
    private int attempts;
    private double highScore;

    public QuizLessonTracker(Lesson lesson) {
        super(lesson);
        this.attempts = 0;
        this.highScore = 0.0;
    }

    public QuizLessonTracker(Lesson lesson, int attempts, double highScore) {
        super(lesson);
        this.attempts = attempts;
        this.highScore = highScore;
    }

    public QuizLessonTracker(Lesson lesson , int attempts, double highScore, boolean state) {
        super(lesson, state);
        this.attempts = attempts;
        this.highScore = highScore;
    }

    private void incrementAttempts(){
        attempts++;
    }

    private void setHigherScore(double highScore){
        if (highScore > this.highScore){
            this.highScore =highScore;
        }
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public double getHighScore() {
        return highScore;
    }

    public void setHighScore(double highScore) {
        this.highScore = highScore;
    }
}
