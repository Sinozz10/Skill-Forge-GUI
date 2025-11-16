import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboard extends DashBoard{
    private JButton viewButton = new JButton();
    private JButton enrollButton = new JButton();

    public StudentDashboard(Student student) {
        super(student);
        navButtons.setLayout(new GridLayout(1,3, 10, 10));

        viewButton.setBackground(Color.LIGHT_GRAY);
        viewButton.setText("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        navButtons.add(viewButton);

        enrollButton.setBackground(Color.LIGHT_GRAY);
        enrollButton.setText("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        navButtons.add(enrollButton);
    }

}
