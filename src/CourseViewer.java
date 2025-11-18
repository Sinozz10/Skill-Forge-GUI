import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CourseViewer extends JPanel {
    private final Course course;
    private final Student student;
    private final CourseDatabaseManager courseDB;
    private final UserDatabaseManager userDB;
    private final DashBoard dashboard;

    public CourseViewer(Course course, Student student, CourseDatabaseManager courseDB, UserDatabaseManager userDB, DashBoard dashboard) {
        this.course = course;
        this.student = student;
        this.courseDB = courseDB;
        this.userDB = userDB;
        this.dashboard = dashboard;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Course: " + course.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();

        if (course.getChapters().isEmpty()) {
            model.addElement("No chapters yet");
        } else {
            Progress progress = student.getProgressTrackerByCourseID(course.getID());
            if (progress != null) {
                progress.updateTrackers(course);
            }
            for (Chapter chapter : course.getChapters()) {
                model.addElement("Chapter " + chapter.getOrder() + ": " + chapter.getChapterID());

                if (chapter.getLessons().isEmpty()) {
                    model.addElement("  No lessons");
                } else {
                    for (Lesson lesson : chapter.getLessons()) {
                        boolean completed = false;
                        if (progress != null) {
                            Tracker tracker = progress.findTracker(lesson);
                            if (tracker != null) {
                                completed = tracker.getState();
                            }
                        }
                        String status = completed ? " [DONE]" : " [ ]";
                        model.addElement("  Lesson " + lesson.getOrder() + ": " + lesson.getTitle() + status);
                    }
                }
            }
        }

        JList<String> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);

        JButton viewButton = new JButton("View Selected Lesson");
        viewButton.setBackground(Color.LIGHT_GRAY);
        viewButton.addActionListener(e -> handleViewLesson(list));

        add(viewButton, BorderLayout.SOUTH);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleViewLesson(list);
                }
            }
        });
    }

}