import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseDelete extends JPanel {
    private JTextField DeleteID;
    private JButton confirmDeleteButton;
    private JPanel delete;
    private final CourseDatabaseManager databaseManager;

    public CourseDelete(CourseDatabaseManager databaseManager, InstructorDashboard dashboard) {
        this.databaseManager = databaseManager;

        setLayout(new BorderLayout());
        add(delete, BorderLayout.CENTER);

        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = DeleteID.getText();
                if(databaseManager.findRecord(id))  {
                    databaseManager.deleteCourse(id);
                } else {
                    JOptionPane.showMessageDialog(dashboard,"Invalid ID");
                }
            }
        });
    }
}
