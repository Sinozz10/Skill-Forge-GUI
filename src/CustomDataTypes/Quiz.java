package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Quiz {
    @Expose
    private ArrayList<Question> questions = new ArrayList<>();

    public Quiz() {
    }

    public double checkAnswers(ArrayList<QuestionTracker> answers){
        if (questions.isEmpty()){
            return 0.0;
        }

        long complete = 0;
        for (QuestionTracker tracker: answers){
            if (getQuestionByTitle(tracker.getID()).checkAnswer(tracker.getAnswer())){
                complete++;
            }
        }

        return (complete * 100.0) / questions.size();
    }

    public Question getQuestionByTitle(String title){
        for (Question question: questions){
            if (question.getTitle().equals(title)){
                return question;
            }
        }
        return null;
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
