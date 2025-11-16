import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorDashboard extends DashBoard{

    public InstructorDashboard(Instructor instructor) {
        super(instructor);
        navButtons.setLayout(new GridLayout(1,3, 10, 10));

        JButton coursesButton = new JButton();
        coursesButton.setBackground(Color.LIGHT_GRAY);
        coursesButton.setText("My Courses");
        coursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        navButtons.add(coursesButton);

        JButton studentsButton = new JButton();
        studentsButton.setBackground(Color.LIGHT_GRAY);
        studentsButton.setText("Students");
        studentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        navButtons.add(studentsButton);
    }
}
