package MainUI;

import CustomDataTypes.*;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class AdminCourseView extends JPanel{
    private JLabel courseTitle;
    private JPanel listPanel;
    private JScrollPane mainScrollPane;
    private JPanel mainPanel;
    private JPanel lessonView;
    private JPanel contentPanel;
    private JLabel lessonTitle;
    private JButton quizButton;
    private JTabbedPane extrasPane;
    private JPanel descriptionPanel;
    private JTextPane descriptionTextPane;
    private JPanel resourcesPanel;
    private JScrollPane resourcesScrollPane;
    private JPanel acvPanel;
    private JPanel adminButtonPanel;
    private JButton rejectButton;
    private JButton approveButton;

    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    private final JTextPane content = new JTextPane();
    private boolean quizViewState;
    private final AdminDashboard dashboard;
    private final GenerationID idGenerator;
    private final Admin admin;
    private final Course course;

    public AdminCourseView(Course course, Admin admin, AdminDashboard dashboard) {
        this.dashboard = dashboard;
        this.admin = admin;
        this.course = course;
        this.idGenerator = new GenerationID();

        setLayout(new BorderLayout());
        add(acvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(32);

        content.setPreferredSize(new Dimension(350, content.getPreferredSize().height));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setEditable(false);

        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.BLACK);

        lessonTitle.setVisible(false);
        lessonTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

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
                        AdminCourseView.this.leftClickHandler(this, lesson);
                    }
                };
                cur.addContent(lp);
                cur.addContent(Box.createRigidArea(new Dimension(0, 5)));
            }
            coursesPanel.add(cur);
        }

        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        courseTitle.setText(course.getTitle());
        descriptionTextPane.setText(course.getDescription());

        rejectButton.addActionListener(e->handleReject());
        rejectButton.setBackground(new Color(120, 7, 5));
        approveButton.addActionListener(e->handleApprove());
        approveButton.setBackground(new Color(7, 120, 5));
    }

    private void handleApprove() {
        int confirm = JOptionPane.showConfirmDialog(
                dashboard,
                "Approve course: " + course.getTitle() + "?",
                "Confirm Approval",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            course.setStatus(StatusCourse.APPROVED);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(
                    dashboard,
                    "Course approved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dashboard.handlePendingCourses();
        }
    }

    private void handleReject() {
        int confirm = JOptionPane.showConfirmDialog(
                dashboard,
                "Reject course: " + course.getTitle() + "?",
                "Confirm Rejection",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            course.setStatus(StatusCourse.REJECTED);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(
                    dashboard,
                    "Course rejected!",
                    "Rejected",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dashboard.handlePendingCourses();
        }
    }

    public void leftClickHandler(LessonPanel Lp, Lesson lesson){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        quizButton.setVisible(false);
        content.setText(lesson.getContent());
        panel.add(content, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        if (lesson.hasQuiz()){
            quizButton.setVisible(true);
            for (ActionListener al : quizButton.getActionListeners()) {
                quizButton.removeActionListener(al);
            }

            quizViewState = false;
            quizButton.addActionListener(_ -> {
                if (quizViewState){
                    content.setText(lesson.getContent());
                    panel.add(content, BorderLayout.CENTER);
                    quizButton.setText("Take Quiz");
                    quizViewState = false;
                    changeContentPanel(panel);
                }else {
                    quizButton.setText("Back");
                    quizViewState = true;
                    changeContentPanel(new QuizPanel(dashboard, lesson, Lp, null));
                }
            });
        }else {
            Lp.setComplete();
        }

        lessonTitle.setVisible(true);
        lessonTitle.setText(lesson.getTitle());
        changeContentPanel(panel);
    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
