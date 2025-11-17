import javax.swing.*;
import java.awt.*;

public class Card extends JPanel{
    private JPanel cardPanel;
    private JLabel instructor;
    private JLabel title;
    private JTextPane description;

    public Card(Course course) {
        setLayout(new BorderLayout());

        if (cardPanel==null){
            cardPanel = new JPanel();

        }else {
            add(cardPanel,BorderLayout.NORTH);
        }

        title.setText(course.getTitle());
        instructor.setText(course.getInstructorID());
        description.setText(course.getDescription());

        description.setEditable(false);
        description.setBackground(Color.GRAY);
    }
}
