package CustomDataTypes;

public class TextQuestion extends Question {
    public TextQuestion(String title, String answer, int order) {
        super(title, answer, order);
        type = QuestionType.TEXT_QUESTION;
    }
}
