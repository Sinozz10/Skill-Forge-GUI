import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Course selected = l.getSelectedValue();

                if (selected == null) {
                    JOptionPane.showMessageDialog(Enroll.this, "Select a course first!");
                    return;
                }


                boolean already = false;
                for (String courseID: student.getCourseIDs()) {
                    if (selected.getID().equals(courseID)) {
                        already = true;
                        break;
                    }
                }

                if (already) {
                    JOptionPane.showMessageDialog(Enroll.this, "You are already enrolled!");
                    return;
                }

                student.addCourse(selected);
                selected.enrollStudent(student);


                databaseManager.updateRecord(selected);
                databaseManager.saveToFile();

                userDb.updateRecord(student);
                userDb.saveToFile();

                JOptionPane.showMessageDialog(Enroll.this, "Enrolled Successfully!");
            }
        });
        add(enrollButton, BorderLayout.SOUTH);
    }
}
