import javax.swing.*;
import java.awt.*;

public class Browse extends JPanel {

    public Browse() {

        CourseDatabaseManager d = new CourseDatabaseManager("courses.json");

        setLayout(new BorderLayout());

        JLabel title = new JLabel("All Courses");

        DefaultListModel<String> model = new DefaultListModel<>();


        for (int i = 0; i < d.getRecords().size(); i++) {
            Course c = d.getRecords().get(i);
            model.addElement(c.getID() + " | " + c.getDescription());
        }

        JList<String> l = new JList<>(model);
        JScrollPane scroll = new JScrollPane(l);

        add(scroll, BorderLayout.CENTER);
    }
}
