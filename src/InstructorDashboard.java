import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorDashboard extends DashBoard{

    public InstructorDashboard(Instructor instructor, CourseDatabaseManager databaseManager) {
        super(instructor);
        navButtons.setLayout(new GridLayout(1,3, 10, 10));

        JButton addButton = new JButton();
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.setText("Add Course");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CourseAdd(databaseManager, instructor.getID()));
            }
        });
        navButtons.add(addButton);

        JButton editButton = new JButton();
        editButton.setBackground(Color.LIGHT_GRAY);
        editButton.setText("Edit Course");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CourseEdit(databaseManager));
            }
        });
        navButtons.add(editButton);

        JButton deleteButton = new JButton();
        deleteButton.setBackground(Color.LIGHT_GRAY);
        deleteButton.setText("Delete Course");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CourseDelete(databaseManager, InstructorDashboard.this));
            }
        });
        navButtons.add(deleteButton);
    }
}
