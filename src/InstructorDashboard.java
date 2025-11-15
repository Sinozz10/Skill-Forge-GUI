import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorDashboard extends DashBoard{
    private JButton coursesButton = new JButton();
    private JButton studentsButton = new JButton();

    public InstructorDashboard() {
        navButtons.setLayout(new GridLayout(1,3, 10, 10));

        coursesButton.setBackground(Color.LIGHT_GRAY);
        coursesButton.setText("My Courses");
        coursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        navButtons.add(coursesButton);

        studentsButton.setBackground(Color.LIGHT_GRAY);
        studentsButton.setText("Students");
        studentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        navButtons.add(studentsButton);
    }

    static void main() {
        new InstructorDashboard();
    }
}
