import javax.swing.*;
import java.awt.*;

public class ViewStudents extends JPanel {

    public ViewStudents(CourseDatabaseManager courseDb, UserDatabaseManager userDb, Instructor instructor) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Enrolled Students", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JPanel studentList = new JPanel();
        studentList.setLayout(new BoxLayout(studentList, BoxLayout.Y_AXIS));

        for (String courseID : instructor.getCourseIDs()) {
            Course crs = courseDb.getRecordByID(courseID);
            if (crs != null) {
                CollapsablePanel courseDropdown = new CollapsablePanel(crs.getTitle());
                for (String studentID : crs.getStudentIDs()){
                    Student stu = (Student) userDb.getRecordByID(studentID);
                    JLabel stuLabel = new JLabel(stu.getID());
                    courseDropdown.addContent(stuLabel);
                }
                studentList.add(courseDropdown);
            }
        }


        add(new JScrollPane(studentList), BorderLayout.CENTER);
    }
}
