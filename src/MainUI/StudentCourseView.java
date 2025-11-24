package MainUI;

import CustomDataTypes.*;
import CustomUIElements.LessonPanel;
import DataManagment.CertificateGenerator;
import DataManagment.GenerationID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

public class StudentCourseView extends CourseView{
    private final JTextPane content = new JTextPane();
    private boolean quizViewState;
    private final StudentDashboard dashboard;
    private final GenerationID idGenerator;
    private final Student student;
    private final Course course;
    protected final Progress progress;

    public StudentCourseView(Course course, Student student, StudentDashboard dashboard) {
        super(course);
        this.dashboard = dashboard;
        this.student = student;
        this.course = course;
        this.idGenerator = new GenerationID();

        progress = student.getProgressTrackerByCourseID(course.getID());

        refreshSidebar();
    }

    private void refreshSidebar() {
        listPanel.removeAll();
        listPanel.add(generateSideBar(), BorderLayout.CENTER);
        listPanel.revalidate();
        listPanel.repaint();
    }

    @Override
    protected LessonPanel generateLessonPanel(Lesson lesson){
        LessonPanel lp = new LessonPanel(lesson){
            @Override
            public void leftClickHandler(MouseEvent e){
                lessonPanelClickHandler(this, lesson, progress);
            }
        };
        if (progress != null){
            LessonTracker tracker = progress.getTrackerByID(lesson.getLessonID());
            if (tracker != null && tracker.isComplete()){
                lp.setComplete();
            }
        }
        return  lp;
    }

    @Override
    protected void lessonPanelClickHandler(LessonPanel Lp, Lesson lesson, Progress progress){
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
            //Create Cert
            Certificate cert = new Certificate(idGenerator.generateCertificateID(),
                    student.getID(),
                    course.getID(),
                    student.getUsername(),
                    course.getTitle()
            );
            student.addCertificate(cert);
            userDB.saveToFile();

            // Generate PDF
            try {
                new CertificateGenerator().certificateGenerator(cert);
                // Ask to view
                if (JOptionPane.showConfirmDialog(this, "Course completed!\nView certificate?",
                        "Congratulations!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(CertificateGenerator.getPath(cert.getCertificateID())));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Certificate saved. View from 'My Certificates'");
            }
        }
    }

}
