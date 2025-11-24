package CustomDataTypes;

public class QuestionTracker extends GeneralTracker {
    private String answer;

    public QuestionTracker(String ID, String answer) {
        super(ID);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
