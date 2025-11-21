package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomUIElements.CollapsablePanel;

import javax.swing.*;
import java.awt.*;
import CustomDataTypes.*;
import CustomUIElements.*;
import DataManagment.*;

public class ViewStudents extends JPanel {
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();

    public ViewStudents(Instructor instructor) {
        setLayout(new BorderLayout());
        setForeground(Color.BLACK);
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Enrolled Students", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setBackground(Color.LIGHT_GRAY);
        add(title, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JPanel studentList = new JPanel();
        studentList.setLayout(new BoxLayout(studentList, BoxLayout.Y_AXIS));

        for (String courseID : instructor.getCourseIDs()) {
            Course crs = courseDB.getRecordByID(courseID);
            if (crs != null) {
                CollapsablePanel courseDropdown = new CollapsablePanel(crs.getID(), crs.getTitle());
                courseDropdown.setBackground(Color.GRAY);
                courseDropdown.setForeground(Color.BLACK);
                for (String studentID : crs.getStudentIDs()){
                    Student stu = (Student) userDB.getRecordByID(studentID);
                    JLabel stuLabel = new JLabel(stu.getID());
                    stuLabel.setForeground(Color.BLACK);
                    courseDropdown.addContent(stuLabel);
                }
                studentList.add(courseDropdown);
            }
        }


        add(new JScrollPane(studentList), BorderLayout.CENTER);
    }
}
