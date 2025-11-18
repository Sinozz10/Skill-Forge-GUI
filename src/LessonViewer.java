import javax.swing.*;
import java.awt.*;

public class LessonViewer extends JPanel {
    private final Lesson lesson;
    private final Student student;
    private final CourseDatabaseManager courseDB;
    private final UserDatabaseManager userDB;
    private final DashBoard dashboard;
    private final Course course;
    private JCheckBox completeCheckbox;

    public LessonViewer(Lesson lesson, Student student, CourseDatabaseManager courseDB, UserDatabaseManager userDB, DashBoard dashboard, Course course) {
        this.lesson = lesson;
        this.student = student;
        this.courseDB = courseDB;
        this.userDB = userDB;
        this.dashboard = dashboard;
        this.course = course;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Lesson: " + lesson.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        Progress progress = student.getProgressTrackerByCourseID(course.getID());
        boolean isCompleted = false;
        if (progress != null) {
            Tracker tracker = progress.getTrackerByID(lesson.getLessonID());
            if (tracker != null) {
                isCompleted = tracker.isComplete();
            }
        }

        completeCheckbox = new JCheckBox("Mark as Completed", isCompleted);
        completeCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(completeCheckbox, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea(lesson.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(contentArea);
        add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton saveButton = new JButton("Save Progress");
        saveButton.setBackground(Color.LIGHT_GRAY);
        saveButton.addActionListener(e -> handleSave());
        buttonPanel.add(saveButton);

        JButton backButton = new JButton("Back to Course");
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.addActionListener(e -> dashboard.changeContentPanel(new CourseViewer(course, student, courseDB, userDB, dashboard)));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSave() {
        Progress progress = student.getProgressTrackerByCourseID(course.getID());
        if (progress == null) {
            JOptionPane.showMessageDialog(this, "Progress tracker not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (completeCheckbox.isSelected()) {
            progress.completeLesson(lesson.getLessonID());
        } else {
            progress.unCompleteLesson(lesson.getLessonID());
        }

        userDB.updateRecord(student);
        userDB.saveToFile();

        JOptionPane.showMessageDialog(this, "Progress saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}