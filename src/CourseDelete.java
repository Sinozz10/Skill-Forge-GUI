import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CourseDelete extends JPanel {
    private JTextField deleteID;
    private JButton confirmDeleteButton;
    private JPanel delete;
    private final CourseDatabaseManager courseDB;
    private final UserDatabaseManager userDB;
    private final InstructorDashboard dashboard;

    public CourseDelete(CourseDatabaseManager courseDB, UserDatabaseManager userDB, Instructor instructor, InstructorDashboard dashboard) {
        this.courseDB = courseDB;
        this.userDB = userDB;
        this.dashboard = dashboard;

        setLayout(new BorderLayout());
        add(delete, BorderLayout.CENTER);

        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete(instructor);
            }
        });
        deleteID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleDelete(instructor);
                }
            }
        });
    }

    private void handleDelete(Instructor instructor) {
        String id = deleteID.getText().trim();
        Course courseToDelete = courseDB.getRecordByID(id);
        if  (id.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard, "Please enter your ID", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(courseDB.findRecord(id))  {
            int confirm = JOptionPane.showConfirmDialog(dashboard,"Are you sure you want to delete?","Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                courseDB.deleteCourse(id);
                courseDB.saveToFile();

                instructor.removeCourse(courseToDelete);
                userDB.updateRecord(instructor);
                userDB.saveToFile();

                JOptionPane.showMessageDialog(dashboard, "Course deleted", "Success", JOptionPane.WARNING_MESSAGE);
                deleteID.setText("");
                deleteID.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(dashboard,"Invalid ID", "Warning", JOptionPane.WARNING_MESSAGE);
            deleteID.setText("");
            deleteID.requestFocus();
        }
    }
}
