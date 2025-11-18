package MainUI;

import CustomDataTypes.Course;
import DataManagment.CourseDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CourseEdit extends JPanel {
    private JTextField courseTitle;
    private JButton editSpecificLessonButton;
    private JButton updateButton;
    private JTextField description;
    private JTextField courseID;
    private JPanel edit;
    private final CourseDatabaseManager databaseManager;

    public CourseEdit(CourseDatabaseManager databaseManager){
        this.databaseManager = databaseManager;

        setLayout(new BorderLayout());
        add(edit, BorderLayout.CENTER);

        editSpecificLessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEdit();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEdit();
            }
        });
        courseID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    courseTitle.requestFocus();
                }
            }
        });
        courseTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    description.requestFocus();
                }
            }
        });
        description.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleEdit();
                }
            }
        });
    }

    private void handleEdit(){
        String id = courseID.getText().trim();
        String title = courseTitle.getText().trim();
        String desc = description.getText().trim();

        if (id.isEmpty() || title.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course c = databaseManager.getRecordByID(courseID.getText());
        if (c == null){
            JOptionPane.showMessageDialog(this, "CustomDataTypes.Course not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c.setTitle(courseTitle.getText());
        c.setDescription(description.getText());
        databaseManager.updateRecord(c);
        databaseManager.saveToFile();
        JOptionPane.showMessageDialog(CourseEdit.this, "CustomDataTypes.Course Edited Successfully!","Success", JOptionPane.INFORMATION_MESSAGE);

        courseID.setText("");
        courseTitle.setText("");
        description.setText("");
        courseID.requestFocus();
    }


}
