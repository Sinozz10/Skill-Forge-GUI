package CustomUIElements;

import CustomDataTypes.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Card extends JPanel{
    private JPanel cardPanel;
    private JLabel id;
    private JLabel title;
    private JTextPane description;
    private JPanel headerPanel;
    private final Course course;

    public Card(Course course, String flavour) {
        this.course = course;
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBackground(Color.LIGHT_GRAY);

        title.setText(flavour != null ? course.getTitle()+"  --  Completion: "+flavour: course.getTitle());
        id.setText("CourseID #: " + course.getID());
        description.setText(course.getDescription());
        description.setEditable(false);

        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 5));

        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClickHandler(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClickHandler(e);
                }
            }
        };

        addMouseListener(clickListener);
        description.addMouseListener(clickListener);

        description.setEnabled(false);
        description.setDisabledTextColor(Color.BLACK);
    }

    public void leftClickHandler(MouseEvent e){
//        System.out.println("left click detected");
    }

    public void rightClickHandler(MouseEvent e){
//        System.out.println("right click detected");
    }

    public Course getCourse() {
        return course;
    }
}
