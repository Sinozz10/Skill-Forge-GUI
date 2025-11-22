package MainUI;

import CustomDataTypes.*;
import DataManagment.CourseDatabaseManager;
import Dialogs.QuestionDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

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
            ArrayList<Question> sortedQuestions = lesson.getQuiz().getQuestions();
            sortedQuestions.sort(Comparator.comparingInt(Question::getOrder));
            for (Question question:sortedQuestions){
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.LIGHT_GRAY);
        deleteButton.setForeground(Color.BLACK);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(dashboard, "Delete Question ?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    lesson.getQuiz().removeQuestion(question);
                    if (lesson.getQuiz().getQuestions().isEmpty()){
                        lesson.setQuiz(null);
                        lesson.setHasQuiz(false);
                    }
                    generatePanel();
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton orderButton = new JButton("Change Order");
        orderButton.setBackground(Color.LIGHT_GRAY);
        orderButton.setForeground(Color.BLACK);
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderChangePopup(question);
            }
        });
        buttonPanel.add(orderButton);


        questionPanel.add(buttonPanel);
        container.add(questionPanel);
        add(container);
    }

    public void generateChoiceQuestion(ChoiceQuestion question){

    }

    private void orderChangePopup(Question question){
        String input = (String) JOptionPane.showInputDialog(this, "Enter new order:", "Change Order",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                question.getOrder()
        );

        if (input != null && !input.trim().isEmpty()) {
            try {
                int newOrder = Integer.parseInt(input.trim());
                int oldOrder = question.getOrder();

                changeQuestionOrder(question, newOrder, oldOrder);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Order must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changeQuestionOrder(Question question, int newOrder, int oldOrder){
        if (newOrder != oldOrder) {
            // Shift all lessons between old and new order
            if (newOrder < oldOrder) {
                // shift down kol lessons from newOrder to oldOrder-1
                for (Question q : lesson.getQuiz().getQuestions()) {
                    if (!q.getTitle().equals(question.getTitle())) {
                        if (q.getOrder() >= newOrder && q.getOrder() < oldOrder) {
                            q.setOrder(q.getOrder() + 1);
                        }
                    }
                }
            } else {
                // shift up
                for (Question q : lesson.getQuiz().getQuestions()) {
                    if (!q.getTitle().equals(question.getTitle())) {
                        if (q.getOrder() > oldOrder && q.getOrder() <= newOrder) {
                            q.setOrder(q.getOrder() - 1);
                        }
                    }
                }
            }
            question.setOrder(newOrder);
            courseDB.saveToFile();
            generatePanel();
        }
    }

    public void handleAdd(){
        Question question = new QuestionDialog(dashboard, lesson).getResult();
        if (question != null) {
            if (lesson.getQuiz() == null){
                lesson.setQuiz(new Quiz());
                lesson.setHasQuiz(true);
            }
            boolean flag = true;
            for(Question q: lesson.getQuiz().getQuestions()){
                if (question.getTitle().equals(q.getTitle())){
                    flag = false;
                    JOptionPane.showMessageDialog(this, "Question must be unique", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (flag){
                int order = question.getOrder();
                question.setOrder(Integer.MAX_VALUE);
                lesson.getQuiz().addQuestion(question);
                changeQuestionOrder(question, order, Integer.MAX_VALUE);
                courseDB.saveToFile();
                generatePanel();
            }
        }
    }

    public void handleDelete(){
        int confirm = JOptionPane.showConfirmDialog(dashboard, "Delete Quiz?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            lesson.setQuiz(null);
            lesson.setHasQuiz(false);
            generatePanel();
        }
    }
}
