import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseAdd extends JPanel {
    private JButton ADDButton;
    private JTextField description;
    private JPanel add;
    private JTextField CourseName;
    private final String instructorId;
    private final CourseDatabaseManager courseDB;
    private final UserDatabaseManager userDB;

    public CourseAdd(CourseDatabaseManager courseDB, UserDatabaseManager userDB, Instructor instructor) {
        this.courseDB = courseDB;
        this.userDB = userDB;
        this.instructorId = instructor.getID();

        setLayout(new BorderLayout());
        add(add,BorderLayout.CENTER);

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddCourse(instructor);
            }
        });
    }

    private void handleAddCourse(Instructor instructor) {
        String courseName = CourseName.getText().trim();
        String courseDescription = description.getText().trim();

        if(courseName.isEmpty() || courseDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid course name!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try{
            String courseID = generateCourseID();
            Course newCourse = new Course(courseID, courseName, courseDescription, instructorId);
            courseDB.addRecord(newCourse);
            courseDB.saveToFile();

            instructor.addCourse(newCourse);
            userDB.updateRecord(instructor);
            userDB.saveToFile();

            JOptionPane.showMessageDialog(this, "Course added successfully!" + courseID, "Success", JOptionPane.INFORMATION_MESSAGE);

            CourseName.setText("");
            CourseName.requestFocus();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error adding course!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String generateCourseID() {
        int highest = getHighestID();// Count existing users of this role
        return String.format("C%04d", highest + 1);
    }

    private int getHighestID() {
        int highest = 0;
        for (Course course : courseDB.getRecords()) {
            String courseId = course.getID();
            // 3mltaha 3ashan t-Extract number from ID
            try {
                String numberPart = courseId.substring(1); // Remove prefix
                int idNumber = Integer.parseInt(numberPart);
                if (idNumber > highest) {
                    highest = idNumber;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return highest;
    }
}
