import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Card extends JPanel{
    private JPanel cardPanel;
    private JLabel instructor;
    private JLabel title;
    private JTextPane description;

    public Card(Course course) {
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
                    leftClickHandler();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClickHandler();
                }
            }
        });
    }

    public void leftClickHandler(){
        System.out.println("left click detected");
    }

    public void rightClickHandler(){
        System.out.println("right click detected");
    }
}
