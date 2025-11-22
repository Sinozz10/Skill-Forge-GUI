package MainUI;

import CustomDataTypes.*;
import DataManagment.CourseDatabaseManager;
import Dialogs.QuestionDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class EditableQuizPanel extends JPanel {
    private final Lesson lesson;
    private final InstructorDashboard dashboard;
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();

    public EditableQuizPanel(InstructorDashboard dashboard, Lesson lesson) {
        this.lesson = lesson;
        this.dashboard = dashboard;
        setLayout(new BoxLayout(EditableQuizPanel.this, BoxLayout.Y_AXIS));

        generatePanel();
    }

    public void generatePanel(){
        removeAll();
        repaint();
        revalidate();

        if(lesson.hasQuiz()){
            for (Question question:lesson.getQuiz().getQuestions()){
                generateQuestionPanel(question);
                add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));

        JButton addButton = new JButton("Add Question");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        JButton deleteButton = new JButton("Delete Quiz");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete();
            }
        });

        wrapper.add(addButton);
        wrapper.add(Box.createRigidArea(new Dimension(10, 0)));
        wrapper.add(deleteButton);
        add(wrapper);
    }

    public void generateQuestionPanel(Question question){
        if (question.getType() == QuestionType.TEXT_QUESTION){
            generateTextQuestion((TextQuestion) question);
        }else {
            generateChoiceQuestion((ChoiceQuestion) question);
        }
    }

    public void generateTextQuestion(TextQuestion question){
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));


        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        JLabel titleLabel = new JLabel("Question:");
        titleLabel.setPreferredSize(new Dimension(70, (int) titleLabel.getPreferredSize().getHeight()));
        titlePanel.add(titleLabel);
        JTextField title = new JTextField();
        title.setText(question.getTitle());
        title.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    question.setTitle(title.getText());
                }
            }
        });
        title.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                question.setTitle(title.getText());
            }
        });
        titlePanel.add(title);
        questionPanel.add(titlePanel);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.X_AXIS));
        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setPreferredSize(new Dimension(70, (int) answerLabel.getPreferredSize().getHeight()));
        answerPanel.add(answerLabel);
        JTextField correctAns = new JTextField();
        correctAns.setText(question.getCorrectAnswer());
        correctAns.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    question.setCorrectAnswer(correctAns.getText());
                }
            }
        });
        correctAns.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                question.setCorrectAnswer(correctAns.getText());
            }
        });
        answerPanel.add(correctAns);
        questionPanel.add(answerPanel);
        container.add(questionPanel);
        add(container);
    }

    public void generateChoiceQuestion(ChoiceQuestion question){

    }

    public void handleAdd(){
        Question question = new QuestionDialog(dashboard, lesson).getResult();
        if (question != null) {
            if (lesson.getQuiz() == null){
                lesson.setQuiz(new Quiz());
                lesson.setHasQuiz(true);
            }
            lesson.getQuiz().addQuestion(question);
            courseDB.saveToFile();
            generatePanel();
        }
    }

    public void handleDelete(){
        lesson.setQuiz(null);
        lesson.setHasQuiz(false);
        generatePanel();
    }
}
