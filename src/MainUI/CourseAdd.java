package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CourseAdd extends JPanel {
    private JButton ADDButton;
    private JTextField description;
    private JPanel add;
    private JTextField CourseName;
    private final String instructorId;
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    private final GenerationID idGenerator;

    public CourseAdd(Instructor instructor) {
        this.instructorId = instructor.getID();
        this.idGenerator = new GenerationID();

        setLayout(new BorderLayout());
        add(add,BorderLayout.CENTER);

        setBackground(Color.LIGHT_GRAY);
        ADDButton.setBackground(Color.LIGHT_GRAY);
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddCourse(instructor);
            }
        });
        CourseName.addKeyListener(new KeyAdapter() {
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
                    handleAddCourse(instructor);
                }
            }
        });
    }

    private void handleAddCourse(Instructor instructor) {
        String courseName = CourseName.getText().trim();
        String courseDescription = description.getText().trim();

        if(courseName.isEmpty() || courseDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid course name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try{
            String courseID = idGenerator.generateCourseID();
            Course newCourse = new Course(courseID, courseName, courseDescription, instructorId);
            courseDB.addRecord(newCourse);
            courseDB.saveToFile();

            instructor.addCourse(newCourse);
            userDB.updateRecord(instructor);
            userDB.saveToFile();

            JOptionPane.showMessageDialog(this, "Course added successfully!" + courseID, "Success", JOptionPane.INFORMATION_MESSAGE);

            CourseName.setText("");
            description.setText("");
            CourseName.requestFocus();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error adding course!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
