package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomUIElements.CollapsablePanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;

public class ViewStudents extends JPanel {

    public ViewStudents(Instructor instructor) {
        setLayout(new BorderLayout());
        setForeground(Color.BLACK);
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Enrolled Students", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setBackground(Color.LIGHT_GRAY);
        add(title, BorderLayout.NORTH);

        JPanel studentList = new JPanel();
        studentList.setLayout(new BoxLayout(studentList, BoxLayout.Y_AXIS));

        for (String courseID : instructor.getCourseIDs()) {
            CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
            Course crs = courseDB.getRecordByID(courseID);
            if (crs != null) {
                CollapsablePanel courseDropdown = getCourseCollapsablePanel(crs);
                studentList.add(courseDropdown);
            }
        }


        add(new JScrollPane(studentList), BorderLayout.CENTER);
    }

    private CollapsablePanel getCourseCollapsablePanel(Course crs) {
        CollapsablePanel courseDropdown = new CollapsablePanel(crs.getID(), crs.getTitle());
        courseDropdown.setBackground(Color.GRAY);
        courseDropdown.setForeground(Color.BLACK);
        for (String studentID : crs.getStudentIDs()) {
            UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
            Student stu = (Student) userDB.getRecordByID(studentID);
            JLabel stuLabel = new JLabel(stu.getID());
            stuLabel.setForeground(Color.BLACK);
            courseDropdown.addContent(stuLabel);
        }
        return courseDropdown;
    }
}
