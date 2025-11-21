package CustomDataTypes;

import java.util.ArrayList;

public class ChoiceQuestion extends Question{
    private ArrayList<String> choices;

    public ChoiceQuestion(String title, String answer) {
        super(title, answer);
    }

    public void addChoice(String choice){
        if (!choices.contains(choice)){
            choices.add(choice);
        }
    }

    public void removeChoice(String choice){
        choices.remove(choice);
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }
}
