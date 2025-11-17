import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Card extends JPanel{
    private JPanel cardPanel;
    private JLabel instructor;
    private JLabel title;
    private JTextPane description;
    private final Course course;

    public Card(Course course) {
        this.course = course;
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        title.setText(course.getTitle());
        instructor.setText("Instructor #: " + course.getInstructorID());
        description.setText(course.getDescription());
        description.setEditable(false);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClickHandler(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClickHandler(e);
                }
            }
        });

        description.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClickHandler(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClickHandler(e);
                }
            }
        });

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
