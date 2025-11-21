package CustomDataTypes;

public class Question {
    protected String title;
    protected String correctAnswer;
    protected String userAnswer = "";

    public Question(String title, String answer) {
        this.title = title;
        this.correctAnswer = answer;
    }

    public boolean checkAnswer(){
        return this.correctAnswer.equals(userAnswer);
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
