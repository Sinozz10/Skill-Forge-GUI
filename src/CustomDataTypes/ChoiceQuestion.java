package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class ChoiceQuestion extends Question {
    @Expose
    private ArrayList<String> choices;

    public ChoiceQuestion(String title, String answer, int order) {
        super(title, answer, order);
        type = QuestionType.CHOICE_QUESTION;
    }

    public void addChoice(String choice) {
        if (!choices.contains(choice)) {
            choices.add(choice);
        }
    }

    public void removeChoice(String choice) {
        choices.remove(choice);
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }
}
