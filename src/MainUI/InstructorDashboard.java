package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import CustomDataTypes.StatusCourse;
import CustomDataTypes.Student;
import CustomUIElements.GenericCard;
import CustomUIElements.CardScrollPane;
import CustomUIElements.CourseCard;
import Statistics.*;
import Statistics.CompletionChartCreation;

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


        navButtons.add(getCoursesButton());
        navButtons.add(getStudentsButton());
        navButtons.add(getInsightButton());

        handleHomeButton();
    }
    private JButton getCoursesButton(){
        JButton viewCoursesButton = new JButton("My Courses");
        viewCoursesButton.setForeground(Color.BLACK);
        viewCoursesButton.setBackground(Color.LIGHT_GRAY);
        viewCoursesButton.addActionListener(_ -> handleViewCourses());
        return viewCoursesButton;
    }

    private JButton getStudentsButton(){
        JButton viewStudentsButton = new JButton("My Students");
        viewStudentsButton.setForeground(Color.BLACK);
        viewStudentsButton.setBackground(Color.LIGHT_GRAY);
        viewStudentsButton.addActionListener(_ -> changeContentPanel(new ViewStudents(instructor)));
        return viewStudentsButton;
    }

    private JButton getInsightButton(){
        JButton insightsButton = new JButton("Insights");
        insightsButton.setForeground(Color.BLACK);
        insightsButton.setBackground(Color.LIGHT_GRAY);
        insightsButton.addActionListener(_ -> handleInsights());
        return insightsButton;
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

        JButton quizzesBtn = new JButton("Quizzes Completion");
        quizzesBtn.setForeground(Color.BLACK);
        quizzesBtn.setBackground(Color.LIGHT_GRAY);

        studentChartBtn.addActionListener(_ -> {
            String sel = (String) courseSelector.getSelectedItem();
            if (sel != null) {
                String id = sel.substring(sel.lastIndexOf("[") + 1, sel.lastIndexOf("]"));
                Course c = courseDB.getRecordByID(id);
                if (c != null) {
                    CompletionChartCreation chartCreator = new CompletionChartCreation("Student Completed", null);
                    org.jfree.chart.JFreeChart chart = chartCreator.createChart(c, userDB,
                            "Student Completed Rates", "Students", "% Complete");
                    new CompletionChartCreation("Student Completed", chart).setVisible(true);
                }
            }
        });

        lessonChartBtn.addActionListener(_ -> {
            String sel = (String) courseSelector.getSelectedItem();
            if (sel != null) {
                String id = sel.substring(sel.lastIndexOf("[") + 1, sel.lastIndexOf("]"));
                Course c = courseDB.getRecordByID(id);
                if (c != null) {
                    LessonChartCreation chartCreator = new LessonChartCreation("Lesson Completed", null);
                    org.jfree.chart.JFreeChart chart = chartCreator.createChart(c, userDB,
                            "Lesson Completed Rates", "Lessons", "%");
                    new LessonChartCreation("Lesson Completed", chart).setVisible(true);
                }
            }
        });

        quizzesBtn.addActionListener(_ -> {
            String sel = (String) courseSelector.getSelectedItem();
            if (sel != null) {
                String id = sel.substring(sel.lastIndexOf("[") + 1, sel.lastIndexOf("]"));
                Course c = courseDB.getRecordByID(id);
                if (c != null) {
                    QuizAnalyticsCompletion chartCreator = new QuizAnalyticsCompletion("Quiz Completed", null);
                    org.jfree.chart.JFreeChart chart = chartCreator.createChart(c, userDB,
                            "Quiz Completed Rates", "Lessons", "%");
                    new QuizAnalyticsCompletion("Quiz Completed", chart).setVisible(true);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Course:"));
        panel.add(courseSelector);
        panel.add(studentChartBtn);
        panel.add(lessonChartBtn);
        panel.add(quizzesBtn);
        changeContentPanel(panel);
    }

    public void handleViewCourses(){
        JButton addCourseBtn = new JButton("Add Course");
        addCourseBtn.setForeground(Color.BLACK);
        addCourseBtn.setBackground(Color.LIGHT_GRAY);
        addCourseBtn.addActionListener(_ -> changeContentPanel(new CourseAdd(instructor)));

        CardScrollPane<Course> pane = new CardScrollPane<>(
                courseDB.getRecords(),
                CourseCard::new, "No Courses Found!",
                course -> "Status: "+course.getStatus().toString(),
                course -> course.getInstructorID().equals(instructor.getID())) {

            @Override
            public void rightClickHandler(MouseEvent e, GenericCard<Course> card){
                // pop up menu
                final JPopupMenu popupMenu = new JPopupMenu();

                //delete item
                JMenuItem deleteItem = new JMenuItem("Delete");
                deleteItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                deleteItem.addActionListener(_ -> {
                    int confirm = JOptionPane.showConfirmDialog(InstructorDashboard.this, "Are you sure you want to delete?",
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
            public void leftClickHandler(MouseEvent e, GenericCard<Course> card){
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
        addPanel.add(addCourseBtn);
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

        JOptionPane.showMessageDialog(InstructorDashboard.this, "Course deleted",
                "Success",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    void handleHomeButton(){
        JLabel userWelcome = new JLabel("Welcome " + instructor.getUsername());
        userWelcome.setFont(new Font("Verdana", Font.PLAIN, 50));
        userWelcome.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JPanel userPanel = new JPanel();
        userPanel.add(userWelcome);
        changeContentPanel(userPanel);
    }
}
