package MainUI;

import CustomDataTypes.*;
import CustomUIElements.CardScrollPane;
import CustomUIElements.CertificateCard;
import CustomUIElements.CourseCard;
import CustomUIElements.GenericCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StudentDashboard extends DashBoard {
    private final Student student;

    public StudentDashboard(Student student) {
        super();
        this.student = student;

        setTitle("Dashboard - " + student.getUsername());
        navButtons.setLayout(new GridLayout(1, 2, 10, 10));
        setBackground(Color.LIGHT_GRAY);
        setResizable(false);

        for (Progress prog : student.getAllProgressTrackers()) {
            prog.updateTrackers(courseDB.getRecordByID(prog.getCourseID()));
        }
        userDB.saveToFile();

        navButtons.add(getViewButton(student));
        navButtons.add(getEnrollButton(student));
        navButtons.add(getCertificatesButton(student));

        handleHomeButton();
    }

    private JButton getViewButton(Student student) {
        JButton viewBtn = new JButton("My Courses");
        viewBtn.setBackground(Color.LIGHT_GRAY);
        viewBtn.setForeground(Color.BLACK);
        viewBtn.addActionListener(_ -> handleViewCourses());

        return viewBtn;
    }

    private JButton getEnrollButton(Student student) {
        JButton enrollButton = new JButton("Enroll");
        enrollButton.setBackground(Color.LIGHT_GRAY);
        enrollButton.setForeground(Color.BLACK);
        enrollButton.addActionListener(_ -> handleViewEnrollableCourses());

        return enrollButton;
    }

    private void handleViewCourses() {
        CardScrollPane<Course> courseScrollPane = new CardScrollPane<>(
                courseDB.getRecords(),
                CourseCard::new,
                "No Courses Found!",
                course -> "Completion: " + student.getProgressTrackerByCourseID(course.getID()).getCompletionPercentage(),
                course -> student.getCourseIDs().contains(course.getID())
                        && course.getStatus() == StatusCourse.APPROVED
        ) {
            @Override
            public void leftClickHandler(MouseEvent e, GenericCard<Course> card) {
                if (e.getClickCount() == 2) {
                    assert card != null;
                    Course selectedCourse = card.getData();
                    changeContentPanel(new StudentCourseView(selectedCourse, student, StudentDashboard.this));
                }
            }
        };

        changeContentPanel(courseScrollPane);
    }

    private void handleViewEnrollableCourses() {

        CardScrollPane<Course> courseScrollPane = new CardScrollPane<>(
                courseDB.getRecords(),
                CourseCard::new,
                "No Courses Found!",
                null,
                course -> !student.getCourseIDs().contains(course.getID())
                        && course.getStatus() == StatusCourse.APPROVED
        ) {
            @Override
            public void leftClickHandler(MouseEvent e, GenericCard<Course> card) {
                if (e.getClickCount() == 2) {
                    int confirm = JOptionPane.showConfirmDialog(StudentDashboard.this, "Are you sure you want to enroll?",
                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        assert card != null;
                        Course selected = card.getData();
                        student.addCourse(selected);
                        selected.enrollStudent(student);

                        courseDB.updateRecord(selected);
                        courseDB.saveToFile();
                        userDB.updateRecord(student);
                        userDB.saveToFile();
                        JOptionPane.showMessageDialog(StudentDashboard.this, "Enrolled Successfully!");
                    }
                }
            }
        };

        changeContentPanel(courseScrollPane);
    }

    private JButton getCertificatesButton(Student student) {
        JButton certificateBtn = new JButton("My Certificates");
        certificateBtn.setBackground(Color.LIGHT_GRAY);
        certificateBtn.setForeground(Color.BLACK);

        certificateBtn.addActionListener(_ -> {
            CardScrollPane<Certificate> certificateScrollPane = new CardScrollPane<>(
                    student.getCertificates(),
                    CertificateCard::new, "No Certificates Found!",
                    null,
                    null
            );
            changeContentPanel(certificateScrollPane);
        });

        return certificateBtn;
    }

    @Override
    void handleHomeButton() {
        JLabel userWelcome = new JLabel("Welcome " + student.getUsername());
        userWelcome.setFont(new Font("Verdana", Font.PLAIN, 50));
        userWelcome.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JPanel userPanel = new JPanel();
        userPanel.add(userWelcome);
        changeContentPanel(userPanel);
    }

    public Student getStudent() {
        return student;
    }
}
