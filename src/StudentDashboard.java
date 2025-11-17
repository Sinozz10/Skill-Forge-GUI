import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboard extends DashBoard{
    public StudentDashboard(Student student, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        super(courseDB, userDB);
        setTitle("Dashboard - " + student.getUsername());
        navButtons.setLayout(new GridLayout(1,3, 10, 10));

        JButton viewButton = new JButton();
        viewButton.setBackground(Color.LIGHT_GRAY);
        viewButton.setText("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new Browse());
            }
        });
        navButtons.add(viewButton);

        JButton enrollButton = new JButton();
        enrollButton.setBackground(Color.LIGHT_GRAY);
        enrollButton.setText("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new Enroll(student));
            }
        });
        navButtons.add(enrollButton);
    }

}
