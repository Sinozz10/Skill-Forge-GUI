import javax.swing.*;
import java.awt.*;

public class ViewStudents extends JPanel {

    public ViewStudents(CourseDatabaseManager courseDb, UserDatabaseManager userDb, Instructor instructor) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Enrolled Students", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String courseID : instructor.getCourseIDs()) {
            Course crs = courseDb.getRecordByID(courseID);
            if (crs != null) {
                listModel.addElement("Course: " + crs.getTitle());
                if (crs.getStudentIDs().isEmpty()) {
                    listModel.addElement("No Students enrolled yet !");
                } else {
                    for (String studentID : crs.getStudentIDs()) {
                        User std = userDb.getRecordByID(studentID);
                        if (std != null) {
                            listModel.addElement(" - " + std.getUsername());
                        }
                    }
                }
            }
        }

        JList<String> studentList = new JList<>(listModel);
        add(new JScrollPane(studentList), BorderLayout.CENTER);
    }
}
