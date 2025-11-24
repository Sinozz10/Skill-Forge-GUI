package MainUI;

import CustomDataTypes.Chapter;
import CustomDataTypes.Course;
import CustomDataTypes.Lesson;
import CustomDataTypes.Progress;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class CourseView extends JPanel{

    protected JLabel courseTitle;
    protected JPanel lessonView;
    protected JScrollPane scrollPane;
    protected JPanel contentPanel;
    protected JTabbedPane extrasPane;
    protected JPanel descriptionPanel;
    protected JPanel resourcesPanel;
    protected JLabel lessonTitle;
    protected JPanel cvPanel;
    protected JPanel listPanel;
    protected JTextPane descriptionTextPane;
    protected JScrollPane resourcesScrollPane;
    protected JPanel mainPanel;
    protected JScrollPane mainScrollPane;
    protected JButton quizButton;
    protected JPanel adminPanel;
    protected JPanel editPanel;

    protected final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    protected final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    protected final JTextPane textContent = new JTextPane();
    protected boolean quizViewState;

    protected final Course course;

    public CourseView(Course course) {
        this.course = course;

        setLayout(new BorderLayout());
        add(cvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(32);

        textContent.setPreferredSize(new Dimension(350, textContent.getPreferredSize().height));
        textContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        textContent.setEditable(false);

        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.BLACK);

        lessonTitle.setVisible(false);
        lessonTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        listPanel.setLayout(new BorderLayout());
        listPanel.add(generateSideBar(), BorderLayout.CENTER);

        courseTitle.setText(course.getTitle());
        descriptionTextPane.setText(course.getDescription());
    }

    protected JScrollPane generateSideBar(){
        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        ArrayList<Chapter> sortedChapters = course.getChapters();
        sortedChapters.sort(Comparator.comparingInt(Chapter::getOrder));
        for (Chapter chapter: sortedChapters){
            CollapsablePanel cur = new CollapsablePanel(chapter.getChapterID(), chapter.getTitle());

            ArrayList<Lesson> sortedLessons = chapter.getLessons();
            sortedLessons.sort(Comparator.comparingInt(Lesson::getOrder));
            for (Lesson lesson: sortedLessons){
                cur.addContent(generateLessonPanel(lesson));
                cur.addContent(Box.createRigidArea(new Dimension(0, 5)));
            }
            coursesPanel.add(cur);
        }
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);

        return scrollPane;
    }

    protected abstract LessonPanel generateLessonPanel(Lesson lesson);

    protected abstract void lessonPanelClickHandler(LessonPanel Lp, Lesson lesson, Progress progress);

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
