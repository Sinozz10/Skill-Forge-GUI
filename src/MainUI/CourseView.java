package MainUI;

import DataManagment.CertificateGenerator;
import java.awt.Desktop;
import java.io.File;

import CustomDataTypes.*;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;
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
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    private final GenerationID idGenerator;
    private final Student student;
    private final Course course;

    public CourseView(Course course, Student student) {
        this.student = student;
        this.course = course;
        this.idGenerator = new GenerationID();
        setLayout(new BorderLayout());
        add(cvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));

        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.BLACK);

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
                Tracker tracker = progress.getTrackerByID(lesson.getLessonID());
                if (tracker != null && tracker.isComplete()){
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

    public void leftClickHandler(MouseEvent e, LessonPanel Lp, Lesson lesson, Progress progress){
        // Show lesson
        JPanel panel = new JPanel(new BorderLayout());
        JTextPane content = new JTextPane();
        content.setText(lesson.getContent());
        content.setEditable(false);
        panel.add(content, BorderLayout.CENTER);

        // Mark completed
        Lp.setComplete();
        Tracker tracker = progress.getTrackerByID(lesson.getLessonID());
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

        // bn-Update UI
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
