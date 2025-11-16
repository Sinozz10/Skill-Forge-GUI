import javax.swing.*;
import java.awt.*;

public class Enrol extends JPanel {

    public Enrol(Student student) {

        CourseDatabaseManager d = new CourseDatabaseManager("courses.json");

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Enroll In a Course");


        DefaultListModel<Course> model = new DefaultListModel<>();


        for (int i = 0; i < d.getRecords().size(); i++) {
            model.addElement(d.getRecords().get(i));
        }

        JList<Course> l = new JList<>(model);
        JScrollPane scroll = new JScrollPane(l);
        add(scroll, BorderLayout.CENTER);

        JButton enrollButton = new JButton("Enroll");
        enrollButton.setBackground(Color.LIGHT_GRAY);

        enrollButton.addActionListener(e -> {

            Course selected = l.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a course first!");
                return;
            }


            boolean already = false;
            for (int i = 0; i < student.getEnrolledCourses().size(); i++) {
                if (student.getEnrolledCourses().get(i).equals(selected.getID())) {
                    already = true;
                    break;
                }
            }

            if (already) {
                JOptionPane.showMessageDialog(this, "You are already enrolled!");
                return;
            }


            student.getEnrolledCourses().add(selected.getID());


            selected.enrollStudent(student);


            d.updateRecord(selected);
            d.saveToFile();

            JOptionPane.showMessageDialog(this, "Enrolled Successfully!");
        });

        add(enrollButton, BorderLayout.SOUTH);
    }
}
