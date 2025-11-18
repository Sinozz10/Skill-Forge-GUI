package MainUI;

import CustomDataTypes.*;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class CourseView extends JPanel{
    private JLabel courseTitle;
    private JPanel lessonView;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JTabbedPane extrasPane;
    private JPanel descriptionPanel;
    private JPanel resourcesPanel;
    private JLabel lessonTitle;
    private JPanel cvPanel;
    private JPanel listPanel;
    private JTextPane descriptionTextPane;
    private JScrollPane resourcesScrollPane;
    private JPanel editPanel;
    private CourseDatabaseManager courseDB;
    private UserDatabaseManager userDB;

    public CourseView(Course course, Student student, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.userDB = userDB;
        this.courseDB = courseDB;

        setLayout(new BorderLayout());
        add(cvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));

        setBackground(Color.LIGHT_GRAY);

        Progress progress = student.getProgressTrackerByCourseID(course.getID());

        lessonTitle.setVisible(false);

        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

        ArrayList<Chapter> sortedChapters = course.getChapters();
        sortedChapters.sort(Comparator.comparingInt(Chapter::getOrder));
        for (Chapter chapter: sortedChapters){
            CollapsablePanel cur = new CollapsablePanel(chapter.getChapterID(), chapter.getTitle());

            ArrayList<Lesson> sortedLessons = chapter.getLessons();
            sortedLessons.sort(Comparator.comparingInt(Lesson::getOrder));
            for (Lesson lesson: sortedLessons){
                LessonPanel lp = new LessonPanel(lesson){
                    @Override
                    public void leftClickHandler(MouseEvent e){
                        CourseView.this.leftClickHandler(e, this, lesson, progress);
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
        descriptionTextPane.setText(course.getDescription());
    }

    public void leftClickHandler(MouseEvent e,LessonPanel Lp, Lesson lesson, Progress progress){
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        JTextPane content = new JTextPane();
        content.setText(lesson.getContent());
        content.setEditable(false);
        tempPanel.add(content, BorderLayout.CENTER);

        Lp.setComplete();
        progress.getTrackerByID(lesson.getLessonID()).setComplete(true);

        lessonTitle.setVisible(true);
        lessonTitle.setText(lesson.getTitle());
        changeContentPanel(tempPanel);
    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
