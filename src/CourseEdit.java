import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseEdit extends CourseDatabaseManager {
    private JTextField title;
    private JButton editSpecificLessonButton;
    private JButton updateButton;
    private JTextField description;
    private JTextField courseID;

    public CourseEdit() {
        editSpecificLessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    Course c = getRecordByID(String.valueOf(courseID));



                    c.setTitle(String.valueOf(title));
                    c.setDescription(String.valueOf(description));

                    updateRecord(c);
                    saveToFile();


                }


        });
    }
}
