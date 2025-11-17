import javax.swing.*;
import java.awt.*;

public class Enroll extends JPanel {

    public Enroll(Student student) {
        UserDatabaseManager userDb = new UserDatabaseManager("users.json");
        CourseDatabaseManager databaseManager = new CourseDatabaseManager("courses.json");
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Enroll In a Course");
        add(title, BorderLayout.NORTH);

        JButton enrollButton = new JButton("Enroll");
        enrollButton.setBackground(Color.LIGHT_GRAY);

        DefaultListModel<Course> model = new DefaultListModel<>();


        for (int i = 0; i < databaseManager.getRecords().size(); i++) {
            model.addElement(databaseManager.getRecords().get(i));
        }

        JList<Course> l = new JList<>(model);
        JScrollPane scroll = new JScrollPane(l);
        add(scroll, BorderLayout.CENTER);

        enrollButton.addActionListener(e -> {
            Course selected = l.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a course first!");
                return;
            }


            boolean already = false;
            for (Course course: student.getCourses()) {
                if (course.getID().equals(selected)) {
                    already = true;
                    break;
                }
            }

            if (already) {
                JOptionPane.showMessageDialog(this, "You are already enrolled!");
                return;
            }

            student.addCourse(selected);
            selected.enrollStudent(student);


            databaseManager.updateRecord(selected);
            databaseManager.saveToFile();

            userDb.updateRecord(student);
            userDb.saveToFile();

            JOptionPane.showMessageDialog(this, "Enrolled Successfully!");
        });
        add(enrollButton, BorderLayout.SOUTH);
    }
}
