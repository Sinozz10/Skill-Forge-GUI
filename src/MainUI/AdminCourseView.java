package MainUI;

import CustomDataTypes.*;
import CustomUIElements.LessonPanel;
import DataManagment.GenerationID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class AdminCourseView extends CourseView{
    private final JTextPane content = new JTextPane();
    private boolean quizViewState;
    private final AdminDashboard dashboard;
    private final GenerationID idGenerator;
    private final Admin admin;

    public AdminCourseView(Course course, Admin admin, AdminDashboard dashboard) {
        super(course);
        this.admin = admin;
        this.dashboard = dashboard;
        this.idGenerator = new GenerationID();

        addButtons();
    }

    private void addButtons(){
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e->handleReject());
        rejectButton.setBackground(new Color(120, 7, 5));
        wrapper.add(rejectButton);

        wrapper.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton approveButton = new JButton("Approve");
        approveButton.addActionListener(e->handleApprove());
        approveButton.setBackground(new Color(7, 120, 5));
        wrapper.add(approveButton);

        adminPanel.setVisible(true);
        adminPanel.removeAll();
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        adminPanel.add(wrapper);
        adminPanel.revalidate();
        adminPanel.repaint();
    }

    private void handleApprove() {
        int confirm = JOptionPane.showConfirmDialog(
                dashboard,
                "Approve course: " + course.getTitle() + "?",
                "Confirm Approval", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            course.setStatus(StatusCourse.APPROVED);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(dashboard, "Course approved successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.handlePendingCourses();
        }
    }

    private void handleReject() {
        int confirm = JOptionPane.showConfirmDialog(dashboard, "Reject course: " + course.getTitle() + "?",
                "Confirm Rejection", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            course.setStatus(StatusCourse.REJECTED);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(dashboard, "Course rejected!",
                    "Rejected", JOptionPane.INFORMATION_MESSAGE);
            dashboard.handlePendingCourses();
        }
    }

    @Override
    protected LessonPanel generateLessonPanel(Lesson lesson) {
        return new LessonPanel(lesson){
            @Override
            public void leftClickHandler(MouseEvent e){
                lessonPanelClickHandler(this, lesson, null);
            }
        };
    }

    @Override
    protected void lessonPanelClickHandler(LessonPanel Lp, Lesson lesson, Progress progress) {
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
}
