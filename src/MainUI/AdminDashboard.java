package MainUI;

import CustomDataTypes.Admin;
import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import CustomDataTypes.StatusCourse;
import CustomUIElements.BaseCard;
import CustomUIElements.CardScrollPane;
import CustomUIElements.CourseCard;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

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
        pendingCoursesButton.addActionListener(_ -> handlePendingCourses());
        navButtons.add(pendingCoursesButton);

        JButton allCoursesButton = new JButton("All Courses");
        allCoursesButton.setForeground(Color.BLACK);
        allCoursesButton.setBackground(Color.LIGHT_GRAY);
        allCoursesButton.addActionListener(_ -> handleAllCourses());
        navButtons.add(allCoursesButton);

        handleHomeButton();
    }

    public void handlePendingCourses() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);

        JLabel titleLabel = new JLabel("Pending Course Approvals", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        CardScrollPane<Course> cardScrollPane = new CardScrollPane<>(
                courseDB.getRecords(),
                CourseCard::new,
                course -> "Status: "+course.getStatus(),
                course -> course.getStatus() == StatusCourse.PENDING) {
            @Override
            public void leftClickHandler(MouseEvent e, BaseCard<Course> card){
                if (e.getClickCount() == 2){
                    assert card != null;
                    Course selectedCourse = card.getData();
                    changeContentPanel(new AdminCourseView(selectedCourse, admin, AdminDashboard.this));
                }
            }
        };
        mainPanel.add(cardScrollPane, BorderLayout.CENTER);

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

    static void main() {
        FlatDarculaLaf.setup();
        UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
        new AdminDashboard((Admin) userDB.getRecordByID("A0001"));
    }
}