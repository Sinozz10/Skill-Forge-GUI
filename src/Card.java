import javax.swing.*;

public class Card extends JPanel{
    private JPanel cardPanel;
    private JLabel instructor;
    private JLabel title;
    private JTextPane description;

    public Card(Course course) {
        title.setText(course.getTitle());
        instructor.setText(course.getInstructorID());
        description.setText(course.getDescription());
    }
}
