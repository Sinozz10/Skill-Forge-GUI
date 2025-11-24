package MainUI;

import CustomDataTypes.*;
import CustomUIElements.LessonPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

public class QuizPanel extends JPanel {
    private final Lesson lesson;
    private final DashBoard dashboard;
    private final LessonPanel Lp;
    private final ArrayList<QuestionTracker> trackers = new ArrayList<>();
    private final Progress progress;

    public QuizPanel(DashBoard dashboard, Lesson lesson, LessonPanel Lp, Progress progress) {
        this.lesson = lesson;
        this.dashboard = dashboard;
        this.Lp = Lp;
        this.progress = progress;
        setLayout(new BoxLayout(QuizPanel.this, BoxLayout.Y_AXIS));

        generatePanel();
    }

    private void generatePanel() {
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

        JButton completeButton = new JButton("Submit");
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleComplete();
            }
        });

        wrapper.add(completeButton);
        add(wrapper);
    }

    private void generateQuestionPanel(Question question) {
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

        questionPanel.add(generateTitlePanel(question));
        questionPanel.add(generateTextPanel(question));

        container.add(questionPanel);
        add(container);
    }

    public void generateChoiceQuestion(ChoiceQuestion question){
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));


        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));

        questionPanel.add(generateTitlePanel(question));
        questionPanel.add(generateDropDownPanel(question));

        container.add(questionPanel);
        add(container);
    }

    private JPanel generateTitlePanel(Question question){
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        JLabel titleLabel = new JLabel("Question:");
        titleLabel.setPreferredSize(new Dimension(70, (int) titleLabel.getPreferredSize().getHeight()));
        titlePanel.add(titleLabel);

        JTextField title = new JTextField();
        title.setText(question.getTitle());
        title.setEditable(false);
        title.setEnabled(false);

        titlePanel.add(title);

        return titlePanel;
    }

    private JPanel generateTextPanel(TextQuestion question){
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.X_AXIS));
        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setPreferredSize(new Dimension(70, (int) answerLabel.getPreferredSize().getHeight()));
        answerPanel.add(answerLabel);

        JTextField userAnswer = new JTextField();
        userAnswer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Optional<QuestionTracker> o = trackers.stream().filter(tracker -> tracker.getID().equals(question.getTitle())).findFirst();
                    if (o.isPresent()){
                        QuestionTracker t = o.get();
                        t.setAnswer(userAnswer.getText());
                    }else {
                        QuestionTracker t = new QuestionTracker(question.getTitle(), userAnswer.getText());
                        trackers.add(t);
                    }
                }
            }
        });
        userAnswer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (trackers.stream().anyMatch(tracker -> tracker.getID().equals(question.getTitle()))){
                    Optional<QuestionTracker> o= trackers.stream().filter(tracker -> tracker.getID().equals(question.getTitle())).findFirst();
                    QuestionTracker t = o.get();
                    t.setAnswer(userAnswer.getText());
                }else {
                    QuestionTracker t = new QuestionTracker(question.getTitle(), userAnswer.getText());
                    trackers.add(t);
                }
            }
        });
        answerPanel.add(userAnswer);
        return answerPanel;
    }

    private JPanel generateDropDownPanel(ChoiceQuestion question){
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.X_AXIS));
        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setPreferredSize(new Dimension(70, (int) answerLabel.getPreferredSize().getHeight()));
        answerPanel.add(answerLabel);

        JComboBox<String> userAnswer = new JComboBox<>();
        userAnswer.addItem("-- Select an option --");
        ArrayList<String> choices = new ArrayList<>(question.getChoices());
        choices.add(question.getCorrectAnswer());
        Collections.shuffle(choices);
        for(String option: choices){
            userAnswer.addItem(option);
        }

        userAnswer.addActionListener(e -> {
            if (userAnswer.getSelectedIndex() == 0) {
                return;
            }
            String selected = (String) userAnswer.getSelectedItem();

            if (trackers.stream().anyMatch(tracker -> tracker.getID().equals(question.getTitle()))){
                Optional<QuestionTracker> o= trackers.stream().filter(tracker -> tracker.getID().equals(question.getTitle())).findFirst();
                QuestionTracker t = o.get();
                t.setAnswer(selected);
            }else {
                QuestionTracker t = new QuestionTracker(question.getTitle(), selected);
                trackers.add(t);
            }
        });

        answerPanel.add(userAnswer);
        return answerPanel;
    }

    private void handleComplete() {
        long complete = 0;
        boolean flag = true;
        for (Question question: lesson.getQuiz().getQuestions()){
            if (trackers.stream().noneMatch(tracker -> tracker.getID().equals(question.getTitle()))){
                JOptionPane.showMessageDialog(dashboard,
                        "All Questions must be answered!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                flag = false;
                break;
            }else {
                Optional<QuestionTracker> o= trackers.stream().filter(tracker -> tracker.getID().equals(question.getTitle())).findFirst();
                QuestionTracker t = o.get();
                if (lesson.getQuiz().getQuestionByTitle(t.getID()).checkAnswer(t.getAnswer())){
                    complete++;
                }
            }
        }
        if (flag){
            double total = (complete * 100.0) / lesson.getQuiz().getQuestions().size();
            if (total >= 70.0){
                Lp.setComplete();
                StudentCourseView parent = null;
                if (progress != null){
                    progress.completeLesson(lesson.getLessonID());
                    Component comp = QuizPanel.this;
                    while (!(comp instanceof StudentCourseView) && comp != null){
                        comp = comp.getParent();
                    }
                    parent = (StudentCourseView) comp;
                    assert parent != null;
                }
                JOptionPane.showMessageDialog(
                        dashboard,
                        "You Passed!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE,
                        UIManager.getIcon("OptionPane.informationIcon")
                );
                if (parent!= null){
                    parent.certificateLogic();
                }
            }else {
                JOptionPane.showMessageDialog(
                        dashboard,
                        "You Failed, Try Again Later",
                        "Fail",
                        JOptionPane.INFORMATION_MESSAGE,
                        UIManager.getIcon("OptionPane.informationIcon")
                );
            }
        }
    }
}
