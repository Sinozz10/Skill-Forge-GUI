import javax.swing.*;
import java.awt.*;

public class CardScrollPane extends JPanel {
    private final CourseDatabaseManager courseDB;
    private JPanel contentPanel;
    private JScrollPane scrollPane;

    public CardScrollPane(CourseDatabaseManager courseDB) {
        this.courseDB = courseDB;

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);

        for (Course course : courseDB.getRecords()) {
            Card card = new Card(course);
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contentPanel.add(card);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
}
