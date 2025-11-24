package CustomDataTypes;

import com.google.gson.annotations.Expose;

public class Attempt {
    @Expose
    private int attemptNum;
    @Expose
    private double score;

    public Attempt(int attemptNum, double score) {
        this.attemptNum = attemptNum;
        this.score = score;
    }

    public int getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(int attemptNum) {
        this.attemptNum = attemptNum;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
