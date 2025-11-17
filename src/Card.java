import javax.swing.*;

public class Card {
    private JPanel cardPanel;
    private JLabel instructor;
    private JLabel title;
    private JTextPane description;
    private final Course course;

    public Card(Course course) {
        this.course = course;

        title.setText(course.getTitle());
        instructor.setText(course.getInstructorID());
        description.setText(course.getDescription());
    }
}
