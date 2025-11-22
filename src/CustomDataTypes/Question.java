package CustomDataTypes;

public class Question {
    protected String title;
    protected String correctAnswer;
    protected int order;
    protected QuestionType type;
    protected transient String userAnswer = "";

    public Question(String title, String answer, int order) {
        this.title = title;
        this.correctAnswer = answer;
        this.order = order;
    }

    public boolean checkAnswer(){
        return correctAnswer.equals(userAnswer);
    }

    public QuestionType getType() {
        return type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
