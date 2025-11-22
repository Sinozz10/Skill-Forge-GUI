package Dialogs;

import CustomDataTypes.Lesson;
import CustomDataTypes.Question;

import javax.swing.*;
import java.awt.*;

public class QuestionDialog extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private Question question;

    public QuestionDialog(Frame parent, Lesson lesson) {
        super(parent,"Adding new Question", true);
        add(contentPanel);
        changeContentPanel(new QuestionTypePanel(QuestionDialog.this, lesson));

        setLocationRelativeTo(this);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
        pack();
    }

    public void setQuestion(Question question){
        this.question = question;
    }

    public Question getResult(){
        return question;
    }
}
