package MainUI;

import CustomDataTypes.Admin;
import CustomDataTypes.StatusCourse;
import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends DashBoard {
    private final Admin admin;
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();

    public AdminDashboard(Admin admin) {
        super();
        this.admin = admin;

        setTitle("Admin Dashboard - " + admin.getUsername());
        navButtons.setLayout(new GridLayout(1, 2, 10, 10));
        setResizable(false);

        JButton pendingCoursesButton = new JButton("Pending Courses");
        pendingCoursesButton.setForeground(Color.BLACK);
        pendingCoursesButton.setBackground(Color.LIGHT_GRAY);
        pendingCoursesButton.addActionListener(e -> handlePendingCourses());
        navButtons.add(pendingCoursesButton);

        JButton allCoursesButton = new JButton("All Courses");
        allCoursesButton.setForeground(Color.BLACK);
        allCoursesButton.setBackground(Color.LIGHT_GRAY);
        allCoursesButton.addActionListener(e -> handleAllCourses());
        navButtons.add(allCoursesButton);

        handleHomeButton();
    }

    private void handlePendingCourses() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);

        JLabel titleLabel = new JLabel("Pending Course Approvals", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(Color.GRAY);
        coursesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boolean hasPending = false;
        for (Course course : courseDB.getRecords()) {
            if (course != null && course.getStatus() == StatusCourse.PENDING) {
                hasPending = true;
                JPanel courseCard = createPendingCourseCard(course);
                coursesPanel.add(courseCard);
                coursesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        if (!hasPending) {
            JLabel noCoursesLabel = new JLabel("No pending courses", SwingConstants.CENTER);
            noCoursesLabel.setForeground(Color.BLACK);
            noCoursesLabel.setBackground(Color.LIGHT_GRAY);
            noCoursesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            coursesPanel.add(noCoursesLabel);
        }

        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        changeContentPanel(mainPanel);
    }

    private void handleAllCourses() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setForeground(Color.BLACK);

        JLabel titleLabel = new JLabel("All Courses in System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create table data
        String[] columnNames = {"Course ID", "Title", "Instructor", "Status", "Students"};
        Object[][] data = new Object[courseDB.getRecords().size()][5];

        int row = 0;
        for (Course course : courseDB.getRecords()) {
            if (course != null) {
                data[row][0] = course.getID();
                data[row][1] = course.getTitle();

                // Safely get instructor name
                String instructorName = "Unknown";
                try {
                    Instructor instructor = (Instructor) userDB.getRecordByID(course.getInstructorID());
                    if (instructor != null) {
                        instructorName = instructor.getUsername();
                    }
                } catch (Exception e) {
                    instructorName = "Error: " + course.getInstructorID();
                }
                data[row][2] = instructorName;

                data[row][3] = course.getStatus() != null ? course.getStatus().toString() : "UNKNOWN";
                data[row][4] = course.getStudentIDs() != null ? course.getStudentIDs().size() : 0;
                row++;
            }
        }

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        changeContentPanel(mainPanel);
    }

    private JPanel createPendingCourseCard(Course course) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel idLabel = new JLabel("ID: " + course.getID());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        idLabel.setForeground(Color.DARK_GRAY);

        // Safely get instructor
        String instructorInfo = course.getInstructorID();
        try {
            Instructor instructor = (Instructor) userDB.getRecordByID(course.getInstructorID());
            if (instructor != null) {
                instructorInfo = instructor.getUsername() + " (" + instructor.getID() + ")";
            }
        } catch (Exception e) {
            instructorInfo = course.getInstructorID() + " (Not Found)";
        }

        JLabel instructorLabel = new JLabel("Instructor: " + instructorInfo);
        instructorLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextArea descArea = new JTextArea(course.getDescription());
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        descArea.setFont(new Font("Arial", Font.PLAIN, 11));
        descArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        descArea.setRows(3);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(idLabel);
        infoPanel.add(instructorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(new JScrollPane(descArea));

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);

        JButton approveButton = new JButton("✓ Approve");
        approveButton.setBackground(new Color(76, 175, 80));
        approveButton.setForeground(Color.WHITE);
        approveButton.setFont(new Font("Arial", Font.BOLD, 12));
        approveButton.setFocusPainted(false);
        approveButton.addActionListener(e -> handleApprove(course));

        JButton rejectButton = new JButton("✗ Reject");
        rejectButton.setBackground(new Color(244, 67, 54));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setFont(new Font("Arial", Font.BOLD, 12));
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> handleReject(course));

        buttonsPanel.add(approveButton);
        buttonsPanel.add(rejectButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonsPanel, BorderLayout.SOUTH);

        return card;
    }

    private void handleApprove(Course course) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Approve course: " + course.getTitle() + "?",
                "Confirm Approval",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            course.setStatus(StatusCourse.APPROVED);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(
                    this,
                    "Course approved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            handlePendingCourses(); // Refresh view
        }
    }

    private void handleReject(Course course) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Reject course: " + course.getTitle() + "?",
                "Confirm Rejection",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            course.setStatus(StatusCourse.REJECTED);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(
                    this,
                    "Course rejected!",
                    "Rejected",
                    JOptionPane.INFORMATION_MESSAGE
            );
            handlePendingCourses(); // Refresh view
        }
    }

    @Override
    void handleHomeButton() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel welcomeLabel = new JLabel("Welcome Admin " + admin.getUsername());
        welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBackground(Color.DARK_GRAY);

        JLabel statsLabel = new JLabel(getSystemStats());
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsLabel.setForeground(Color.BLACK);
        statsLabel.setBackground(Color.DARK_GRAY);

        homePanel.add(welcomeLabel, gbc);
        homePanel.add(statsLabel, gbc);

        changeContentPanel(homePanel);
    }

    private String getSystemStats() {
        long pendingCount = courseDB.getRecords().stream()
                .filter(c -> c != null && c.getStatus() == StatusCourse.PENDING)
                .count();

        long approvedCount = courseDB.getRecords().stream()
                .filter(c -> c != null && c.getStatus() == StatusCourse.APPROVED)
                .count();

        long rejectedCount = courseDB.getRecords().stream()
                .filter(c -> c != null && c.getStatus() == StatusCourse.REJECTED)
                .count();

        return String.format(
                "<html><center>System Statistics<br><br>" +
                        "Total Courses: %d<br>" +
                        "Pending: %d || Approved: %d || Rejected: %d<br>" +
                        "Total Users: %d</center></html>",
                courseDB.getRecords().size(),
                pendingCount, approvedCount, rejectedCount,
                userDB.getRecords().size()
        );
    }
}