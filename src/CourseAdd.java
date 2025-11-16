import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseAdd extends JPanel {
    private JButton ADDButton;
    private JTextField title;
    private JTextField description;
    private CourseDatabaseManager databaseManager;

    public CourseAdd(CourseDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                    String courseId = "C" + (records.size() + 1);

                    Course newCourse = new Course(courseId, title, description);

                    addRecord(newCourse);
                    saveToFile();

                    return newCourse;


            }
        });
    }
}
