package CustomUIElements;

import CustomDataTypes.Course;

import javax.swing.*;
import java.awt.*;

public class CourseCard extends GenericCard<Course> {
    private JPanel cardPanel;
    private JLabel id;
    private JLabel title;
    private JTextPane description;
    private JPanel headerPanel;

    public CourseCard(Course course, String flavour) {
        super(course);
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        title.setText(flavour != null ? course.getTitle() + "  --  " + flavour : course.getTitle());
        id.setText("CourseID #" + course.getID());

        description.setText(course.getDescription());
        description.setBackground(Color.lightGray);
        description.setEditable(false);
        description.setEnabled(false);
        description.setDisabledTextColor(Color.BLACK);

        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 5));
    }

    @Override
    public String getSearchableText() {
        return data.getTitle();
    }
}
