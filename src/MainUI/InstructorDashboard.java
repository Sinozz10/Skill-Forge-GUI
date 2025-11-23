package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import CustomDataTypes.StatusCourse;
import CustomDataTypes.Student;
import CustomUIElements.BaseCard;
import CustomUIElements.CardScrollPane;
import CustomUIElements.CourseCard;
import DataManagment.UserDatabaseManager;
import Statistics.ChartStatistics;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class InstructorDashboard extends DashBoard{
    private final Instructor instructor;

    public InstructorDashboard(Instructor instructor) {
        super();
        this.instructor = instructor;

        setTitle("Dashboard - " + instructor.getUsername());
        setBackground(Color.GRAY);
        navButtons.setLayout(new GridLayout(1,3, 10, 10));
        setResizable(false);

        JButton viewCoursesButton = new JButton("My Courses");
        viewCoursesButton.setForeground(Color.BLACK);
        viewCoursesButton.setBackground(Color.LIGHT_GRAY);
        viewCoursesButton.addActionListener(_ -> handleViewCourses());
        navButtons.add(viewCoursesButton);

        JButton viewStudentsButton = new JButton("My Students");
        viewStudentsButton.setForeground(Color.BLACK);
        viewStudentsButton.setBackground(Color.LIGHT_GRAY);
        viewStudentsButton.addActionListener(_ -> changeContentPanel(new ViewStudents(instructor)));
        navButtons.add(viewStudentsButton);

        JButton insightsButton = new JButton("Insights");
        insightsButton.setForeground(Color.BLACK);
        insightsButton.setBackground(Color.LIGHT_GRAY);
        insightsButton.addActionListener(e -> handleInsights());
        navButtons.add(insightsButton);

        handleHomeButton();
    }

    private void handleInsights() {
        JComboBox<String> courseSelector = new JComboBox<>();
        courseSelector.setForeground(Color.BLACK);
        courseSelector.setBackground(Color.GRAY);
        for (String id : instructor.getCourseIDs()) {
            Course c = courseDB.getRecordByID(id);
            if (c != null && c.getStatus() == StatusCourse.APPROVED) {
                courseSelector.addItem(c.getTitle() + " [" + id + "]");
            }
        }

        JButton studentChartBtn = new JButton("Student Completion");
        studentChartBtn.setForeground(Color.BLACK);
        studentChartBtn.setBackground(Color.LIGHT_GRAY);
        JButton lessonChartBtn = new JButton("Lesson Completion");
        lessonChartBtn.setForeground(Color.BLACK);
        lessonChartBtn.setBackground(Color.LIGHT_GRAY);

        studentChartBtn.addActionListener(e -> {
            String sel = (String) courseSelector.getSelectedItem();
            if (sel != null) {
                String id = sel.substring(sel.lastIndexOf("[") + 1, sel.lastIndexOf("]"));
                Course c = courseDB.getRecordByID(id);
                if (c != null) {
                    org.jfree.chart.JFreeChart chart = ChartStatistics.createCompletionChart(c, userDB);
                    new ChartStatistics("Student Completion", chart).setVisible(true);
                }
            }
        });

        lessonChartBtn.addActionListener(e -> {
            String sel = (String) courseSelector.getSelectedItem();
            if (sel != null) {
                String id = sel.substring(sel.lastIndexOf("[") + 1, sel.lastIndexOf("]"));
                Course c = courseDB.getRecordByID(id);
                if (c != null) {
                    org.jfree.chart.JFreeChart chart = ChartStatistics.createLessonChart(c, userDB);
                    new ChartStatistics("Lesson Completion", chart).setVisible(true);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Course:"));
        panel.add(courseSelector);
        panel.add(studentChartBtn);
        panel.add(lessonChartBtn);
        changeContentPanel(panel);
    }


    public void handleViewCourses(){
        JButton addButton = new JButton("Add Course");
        addButton.setForeground(Color.BLACK);
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.addActionListener(_ -> changeContentPanel(new CourseAdd(instructor)));
        CardScrollPane<Course> pane = new CardScrollPane<>(
                courseDB.getRecords(),
                CourseCard::new,
                course -> "Status: "+course.getStatus().toString(),
                course -> course.getInstructorID().equals(instructor.getID())) {
            @Override
            public void rightClickHandler(MouseEvent e, BaseCard<Course> card){
                // pop up menu
                final JPopupMenu popupMenu = new JPopupMenu();

                //delete item
                JMenuItem deleteItem = new JMenuItem("Delete");
                deleteItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                deleteItem.addActionListener(_ -> {
                    int confirm = JOptionPane.showConfirmDialog(InstructorDashboard.this,
                            "Are you sure you want to delete?",
                            "Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        assert card != null;
                        handleDelete(instructor, card.getData());
                    }

                });
                popupMenu.add(deleteItem);

                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void leftClickHandler(MouseEvent e, BaseCard<Course> card){
//                Component comp = e.getComponent();
//                while (!(comp instanceof CourseCard) && comp != null){
//                    comp = comp.getParent();
//                }
//                final CourseCard clickedCard = (CourseCard) comp;
                if (e.getClickCount() == 2){
                    assert card != null;
                    Course selectedCourse = card.getData();
                    changeContentPanel(new EditableCourseView(selectedCourse, instructor, InstructorDashboard.this));
                }
            }
        };

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        viewPanel.add(pane);
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
        addPanel.add(addButton);
        viewPanel.add(addPanel);

        changeContentPanel(viewPanel);
    }

    private void handleDelete(Instructor instructor, Course course) {
        String id = course.getID();
        Course courseToDelete = courseDB.getRecordByID(id);

        courseDB.deleteRecord(id);
        courseDB.saveToFile();

        instructor.removeCourse(courseToDelete);
        userDB.updateRecord(instructor);
        for (String studentID: courseToDelete.getStudentIDs()){
            System.out.println(studentID);
            Student student = (Student) userDB.getRecordByID(studentID);
            student.removeCourse(courseToDelete);
            userDB.updateRecord(student);
        }
        userDB.saveToFile();

        JOptionPane.showMessageDialog(InstructorDashboard.this,
                "Course deleted",
                "Success",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    void handleHomeButton(){
        JLabel userLabel = new JLabel("Welcome " + instructor.getUsername());
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        userLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JPanel userPanel = new JPanel();
        userPanel.add(userLabel);
        changeContentPanel(userPanel);
    }

    static void main() {
        FlatDarculaLaf.setup();
        UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
        new InstructorDashboard((Instructor) userDB.getRecordByID("I0001"));
    }
}
