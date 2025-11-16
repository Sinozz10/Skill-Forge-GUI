import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseDelete extends JPanel {
    private JTextField DeleteID;
    private JButton confirmDeleteButton;
    private CourseDatabaseManager databaseManager;

    public CourseDelete(CourseDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(findRecord(String.valueOf(DeleteID)))
                {
                    deleteCourse(String.valueOf(DeleteID));
                }
                else
                {
                    JOptionPane.showMessageDialog(frame,"Invalid ID");
                }
            }
        });
    }
}
