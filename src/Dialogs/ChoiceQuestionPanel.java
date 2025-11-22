package Dialogs;

import javax.swing.*;
import java.awt.*;

public class ChoiceQuestionPanel extends JPanel {
    private QuestionDialog parent;
    private final JPanel contentPanel = new JPanel();

    public ChoiceQuestionPanel(QuestionDialog parent) {
        this.parent = parent;

        initComponents();
        addListeners();
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    public void initComponents(){

    }

    public void addListeners(){

    }
}
