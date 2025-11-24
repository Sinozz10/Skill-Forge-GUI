package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CourseAdd extends JPanel {
    private JButton addBtn;
    private JTextField description;
    private JPanel add;
    private JTextField courseName;
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

        addBtn.setBackground(Color.LIGHT_GRAY);
        addBtn.setForeground(Color.BLACK);
        addBtn.addActionListener(_ -> handleAddCourse(instructor));

        courseName.addKeyListener(new KeyAdapter() {
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
        String courseName = this.courseName.getText().trim();
        String courseDescription = description.getText().trim();

        if(courseName.isEmpty() || courseDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid course name!",
                    "Error", JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(this, "Course added successfully!" + courseID,
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            this.courseName.setText("");
            description.setText("");
            this.courseName.requestFocus();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error adding course!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
