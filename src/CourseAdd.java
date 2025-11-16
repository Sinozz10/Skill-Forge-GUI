import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseAdd extends CourseDatabaseManager {
    private JButton ADDButton;
    private JTextField title;
    private JTextField description;

    public CourseAdd() {
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
