import javax.swing.*;
import java.awt.*;

public class CardScrollPane extends JFrame {
    private final CourseDatabaseManager courseDB;
    private JPanel contentPanel;
    private JScrollPane scrollPane;

    public CardScrollPane(CourseDatabaseManager courseDB) {
        this.courseDB = courseDB;

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);

        for (Course course : courseDB.getRecords()) {
            Card card = new Card(course);
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contentPanel.add(card);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            System.out.println("Added card for: " + course.getTitle());
        }

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        add(scrollPane, BorderLayout.CENTER);

        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void main() {
        new CardScrollPane(new CourseDatabaseManager("courses.json"));
    }
}
