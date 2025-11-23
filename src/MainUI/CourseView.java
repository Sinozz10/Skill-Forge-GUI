package MainUI;

import CustomDataTypes.*;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CertificateGenerator;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
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
    private JPanel mainPanel;
    private JScrollPane mainScrollPane;
    private JButton quizButton;
    private JPanel editPanel;

    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    private final JTextPane content = new JTextPane();
    private boolean quizViewState;
    private final StudentDashboard dashboard;
    private final GenerationID idGenerator;
    private final Student student;
    private final Course course;

    public CourseView(Course course, Student student, StudentDashboard dashboard) {
        this.dashboard = dashboard;
        this.student = student;
        this.course = course;
        this.idGenerator = new GenerationID();

        setLayout(new BorderLayout());
        add(cvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(32);

        content.setPreferredSize(new Dimension(350, content.getPreferredSize().height));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setEditable(false);

        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.BLACK);

        Progress progress = student.getProgressTrackerByCourseID(course.getID());

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
                        CourseView.this.leftClickHandler(this, lesson, progress);
                    }
                };
                LessonTracker tracker = progress.getTrackerByID(lesson.getLessonID());
                if (tracker != null && tracker.isComplete()){
                    lp.setComplete();
                }
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
    }

    public void leftClickHandler(LessonPanel Lp, Lesson lesson, Progress progress){
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
                    changeContentPanel(new QuizPanel(dashboard, lesson, Lp, progress));
                }
            });
        }else {
            temporaryMethodName(Lp, lesson, progress);
        }

        lessonTitle.setVisible(true);
        lessonTitle.setText(lesson.getTitle());
        changeContentPanel(panel);
    }

    public void temporaryMethodName(LessonPanel Lp, Lesson lesson, Progress progress){
        Lp.setComplete();
        LessonTracker tracker = progress.getTrackerByID(lesson.getLessonID());
        if (tracker != null) {
            tracker.setComplete(true);
        }
        progress.getTrackerByID(lesson.getLessonID()).setComplete(true);
        userDB.updateRecord(student);
        userDB.saveToFile();

        // Check if course complete
        if (progress.isCourseComplete() && student.getCertificateByCourseID(course.getID()) == null) {
            // Create certificate
            Certificate cert = new Certificate(idGenerator.generateCertificateID(),
                    student.getID(),
                    course.getID(),
                    student.getUsername(),
                    course.getTitle()
            );

            student.addCertificate(cert);
            userDB.saveToFile();

            // Try to generate PDF
            try {
                new CertificateGenerator().generateCertificate(cert);
                // Ask to view
                if (JOptionPane.showConfirmDialog(this,
                        "Course completed!\nView certificate?",
                        "Congratulations!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(CertificateGenerator.getPath(cert.getCertificateID())));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Certificate saved. View from 'My Certificates'");
            }
        }
    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
