import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class CourseViewFrame extends JPanel{
    private JLabel courseTitle;
    private JPanel lessonView;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JTabbedPane extrasPane;
    private JPanel descriptionPanel;
    private JPanel resourcesPanel;
    private JLabel lessonTitle;
    private JPanel lvPanel;
    private JPanel listPanel;
    private JTextPane descriptionTestPane;
    private JScrollPane resourcesScrollPane;

    public CourseViewFrame(Course course, Progress progress) {
        setLayout(new BorderLayout());
        add(lvPanel, BorderLayout.CENTER);

        lessonTitle.setVisible(false);

        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

        ArrayList<Chapter> sortedChapters = course.getChapters();
        sortedChapters.sort(Comparator.comparingInt(Chapter::getOrder));
        for (Chapter chapter: sortedChapters){
            CollapsablePanel cur = new CollapsablePanel(chapter.getTitle());

            ArrayList<Lesson> sortedLessons = chapter.getLessons();
            sortedLessons.sort(Comparator.comparingInt(Lesson::getOrder));
            for (Lesson lesson: sortedLessons){
                LessonPanel lp = new LessonPanel(lesson){
                    @Override
                    public void leftClickHandler(MouseEvent e){
                        JPanel tempPanel = new JPanel();
                        tempPanel.setLayout(new BorderLayout());
                        JTextPane content = new JTextPane();
                        content.setText(lesson.getContent());
                        content.setEditable(false);
                        tempPanel.add(content, BorderLayout.CENTER);

                        setComplete();
                        progress.getTrackerByID(lesson.getLessonID()).setComplete(true);

                        lessonTitle.setVisible(true);
                        lessonTitle.setText(lesson.getTitle());
                        changeContentPanel(tempPanel);
                    }
                };
                if (progress.getTrackerByID(lesson.getLessonID()).isComplete()){
                    lp.setComplete();
                }
                cur.addContent(lp);
                cur.addContent(Box.createRigidArea(new Dimension(0, 5)));
            }
            coursesPanel.add(cur);
        }

        scrollPane = new JScrollPane(coursesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        courseTitle.setText(course.getTitle());
        descriptionTestPane.setText(course.getDescription());

    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Course View Demo");
            frame.setSize(700, 500);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            CourseDatabaseManager courseDB = new CourseDatabaseManager("courses.json");
            UserDatabaseManager userDB = new UserDatabaseManager("users.json");

            Course course = courseDB.getRecordByID("C0001");
            Student student = (Student) userDB.getRecordByID("S0001");
            for (Progress prog: student.getAllProgressTrackers()){
                prog.updateTrackers(courseDB.getRecordByID(prog.getCourseID()));
            }
            userDB.saveToFile();

            Progress progress = student.getProgressTrackerByCourseID("C0001");
            CourseViewFrame panel1 = new CourseViewFrame(course, progress);

            frame.add(panel1);
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int option = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to close the application?",
                            "Close Confirmation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (option == JOptionPane.YES_OPTION) {
                        userDB.saveToFile();
                        courseDB.saveToFile();
                        System.exit(0);
                    }
                }
            });
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
