package CustomDataTypes;

import java.util.ArrayList;

public class Quiz {
    private String title;
    private ArrayList<Question> questions;

    public Quiz(String title) {
        this.title = title;
    }

    public double getGrade(){
        long complete = questions.stream()
                .filter(Question::checkAnswer)
                .count();
        if (questions.isEmpty()){
            return 0.0;
        }
        return (complete * 100.0) / questions.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addChoice(Question question){
        if (!questions.contains(question)){
            questions.add(question);
        }
    }

    public void removeChoice(Question question){
        questions.remove(question);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
