package CustomDataTypes;

import java.util.ArrayList;

public class Quiz {
    private ArrayList<Question> questions = new ArrayList<>();

    public Quiz() {
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

    public void addQuestion(Question question){
        if (!questions.contains(question)){
            questions.add(question);
        }
    }

    public void removeQuestion(Question question){
        questions.remove(question);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
