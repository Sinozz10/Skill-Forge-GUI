package Dialogs;

import CustomDataTypes.Attempt;
import CustomDataTypes.LessonTracker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class AttemptsDialog extends JDialog {
    private final LessonTracker tracker;

    public AttemptsDialog(Frame parent, LessonTracker tracker) {
        super(parent, "Previous Attempts & Scores", true);
        this.tracker = tracker;

        initComponents();

        pack();
        setLocationRelativeTo(this);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents(){
        setLayout(new BorderLayout(10, 10));

        JScrollPane scrollPane = new JScrollPane();
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        ArrayList<Attempt> sorted = new ArrayList<>(tracker.getAttempts());
        sorted.sort(Comparator.comparingInt(Attempt::getAttemptNum));
        System.out.println(sorted);
        for (Attempt attempt: sorted){
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.add(new JLabel(String.valueOf(attempt.getAttemptNum())));
            row.add(Box.createRigidArea(new Dimension(10, 0)));
            row.add(new JLabel(String.valueOf(attempt.getScore())));

            wrapper.add(row);
            wrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        scrollPane.add(wrapper);
        add(scrollPane, BorderLayout.CENTER);
    }
}
