package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Progress;
import CustomDataTypes.Student;
import CustomUIElements.Card;
import CustomUIElements.CardScrollPane;
import DataManagment.UserDatabaseManager;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StudentDashboard extends DashBoard{
    private final Student student;

    public StudentDashboard(Student student) {
        super();
        this.student = student;

        setTitle("Dashboard - " + student.getUsername());
        navButtons.setLayout(new GridLayout(1,2, 10, 10));
        setBackground(Color.LIGHT_GRAY);
        setResizable(false);

        for (Progress prog: student.getAllProgressTrackers()){
            prog.updateTrackers(courseDB.getRecordByID(prog.getCourseID()));
        }
        userDB.saveToFile();

        JButton viewButton = getViewButton(student);
        navButtons.add(viewButton);

        JButton enrollButton = getEnrollButton(student);
        navButtons.add(enrollButton);

        handleHomeButton();
    }

    private JButton getViewButton(Student student) {
        JButton viewButton = new JButton("My Courses");
        viewButton.setBackground(Color.LIGHT_GRAY);
        viewButton.setForeground(Color.BLACK);
        viewButton.addActionListener(e -> changeContentPanel(new CardScrollPane(student, course -> student.getCourseIDs().contains(course.getID())){
            @Override
            public void leftClickHandler(MouseEvent e){
                Component comp = e.getComponent();
                while (!(comp instanceof Card) && comp != null){
                    comp = comp.getParent();
                }
                final Card clickedCard = (Card) comp;
                if (e.getClickCount() == 2){
                    assert clickedCard != null;
                    Course selectedCourse = clickedCard.getCourse();
                    changeContentPanel(new CourseView(selectedCourse, student, StudentDashboard.this));
                }
            }
        }));
        return viewButton;
    }

    private JButton getEnrollButton(Student student){
        JButton enrollButton = new JButton("Enroll");
        enrollButton.setBackground(Color.LIGHT_GRAY);
        enrollButton.setForeground(Color.BLACK);
        enrollButton.addActionListener(e -> changeContentPanel(new CardScrollPane(student, course->!student.getCourseIDs().contains(course.getID())){
            @Override
            public void leftClickHandler(MouseEvent e){
                Component comp = e.getComponent();
                while (!(comp instanceof Card) && comp != null){
                    comp = comp.getParent();
                }
                final Card clickedCard = (Card) comp;
                if (e.getClickCount() == 2){
                    int confirm = JOptionPane.showConfirmDialog(StudentDashboard.this,
                            "Are you sure you want to enroll?",
                            "Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        assert clickedCard != null;
                        Course selected = clickedCard.getCourse();
                        student.addCourse(selected);
                        selected.enrollStudent(student);


                        courseDB.updateRecord(selected);
                        courseDB.saveToFile();

                        userDB.updateRecord(student);
                        userDB.saveToFile();

                        loadCoursesFromDatabase();
                        JOptionPane.showMessageDialog(StudentDashboard.this, "Enrolled Successfully!");
                    }
                }
            }
        }));
        return enrollButton;
    }

    @Override
    void handleHomeButton(){
        JLabel userLabel = new JLabel("Welcome "+student.getUsername());
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        userLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JPanel userPanel = new JPanel();
        userPanel.add(userLabel);
        changeContentPanel(userPanel);
    }

    public Student getStudent(){
        return student;
    }

    static void main() {
        FlatDarculaLaf.setup();
        UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
        new StudentDashboard((Student) userDB.getRecordByID("S0001"));
    }
}
