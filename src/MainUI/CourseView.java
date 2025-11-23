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
    protected JPanel editPanel;

    protected final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    protected final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    protected final JTextPane textContent = new JTextPane();
    protected boolean quizViewState;
    protected final StudentDashboard dashboard;
    protected final GenerationID idGenerator;
    protected final Student student;
    protected final Course course;
    protected final Progress progress;

    public CourseView(Course course, Student student, StudentDashboard dashboard) {
        this.dashboard = dashboard;
        this.student = student;
        this.course = course;
        this.idGenerator = new GenerationID();

        setLayout(new BorderLayout());
        add(cvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(32);

        textContent.setPreferredSize(new Dimension(350, textContent.getPreferredSize().height));
        textContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        textContent.setEditable(false);

        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.BLACK);

        progress = student.getProgressTrackerByCourseID(course.getID());

        lessonTitle.setVisible(false);
        lessonTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        listPanel.setLayout(new BorderLayout());
        listPanel.add(generateSideBar(), BorderLayout.CENTER);

        courseTitle.setText(course.getTitle());
        descriptionTextPane.setText(course.getDescription());
    }

    private JScrollPane generateSideBar(){
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

    private LessonPanel generateLessonPanel(Lesson lesson){
        LessonPanel lp = new LessonPanel(lesson){
            @Override
            public void leftClickHandler(MouseEvent e){
                lessonPanelClickHandler(this, lesson, progress);
            }
        };
        LessonTracker tracker = progress.getTrackerByID(lesson.getLessonID());
        if (tracker != null && tracker.isComplete()){
            lp.setComplete();
        }
        return  lp;
    }

    private void lessonPanelClickHandler(LessonPanel Lp, Lesson lesson, Progress progress){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        quizButton.setVisible(false);
        textContent.setText(lesson.getContent());
        panel.add(textContent, BorderLayout.CENTER);
        panel.add(textContent, BorderLayout.CENTER);

        if (lesson.hasQuiz()){
            hasQuizLogic(panel, lesson, Lp);
        }else {
            hasNoQuizLogic(Lp, lesson, progress);
        }

        lessonTitle.setVisible(true);
        lessonTitle.setText(lesson.getTitle());
        changeContentPanel(panel);
    }

    private void hasQuizLogic(JPanel panel, Lesson lesson, LessonPanel Lp){
        quizButton.setVisible(true);
        for (ActionListener al : quizButton.getActionListeners()) {
            quizButton.removeActionListener(al);
        }

        quizViewState = false;
        quizButton.addActionListener(_ -> {
            if (quizViewState){
                textContent.setText(lesson.getContent());
                panel.add(textContent, BorderLayout.CENTER);
                quizButton.setText("Take Quiz");
                quizViewState = false;
                changeContentPanel(panel);
            }else {
                quizButton.setText("Back");
                quizViewState = true;
                changeContentPanel(new QuizPanel(dashboard, lesson, Lp, progress));
            }
        });
    }

    private void hasNoQuizLogic(LessonPanel Lp, Lesson lesson, Progress progress){
        Lp.setComplete();
        LessonTracker tracker = progress.getTrackerByID(lesson.getLessonID());
        if (tracker != null) {
            tracker.setComplete(true);
        }
        progress.getTrackerByID(lesson.getLessonID()).setComplete(true);
        userDB.updateRecord(student);
        userDB.saveToFile();

        certificateLogic();
    }

    private void certificateLogic(){
        // Check if course is complete
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
