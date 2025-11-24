package Dialogs;

import javax.swing.*;
import java.awt.*;

public class QuestionTypePanel extends JPanel {
    private final QuestionDialog parent;
    private JButton choice;
    private JButton text;
    private JButton cancelButton;
    private final JPanel contentPanel = new JPanel();

    public QuestionTypePanel(QuestionDialog parent) {
        this.parent = parent;

        initComponents();
        addListeners();
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    public void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 1, 10, 10));

        choice = new JButton("Multi Choice");
        choice.setBackground(Color.LIGHT_GRAY);
        choice.setForeground(Color.BLACK);
        formPanel.add(choice);

        text = new JButton("Text Based");
        text.setBackground(Color.LIGHT_GRAY);
        text.setForeground(Color.BLACK);
        formPanel.add(text);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        formPanel.add(cancelButton);

        contentPanel.add(formPanel, BorderLayout.CENTER);
    }

    public void addListeners() {
        choice.addActionListener(_ -> parent.changeContentPanel(new ChoiceQuestionPanel(parent)));

        text.addActionListener(_ -> parent.changeContentPanel(new TextQuestionPanel(parent)));

        cancelButton.addActionListener(_ -> handleCancel());
    }

    private void handleCancel() {
        parent.dispose();
    }
}
