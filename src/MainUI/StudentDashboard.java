package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Progress;
import CustomDataTypes.Student;
import CustomUIElements.Card;
import CustomUIElements.CardScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import CustomDataTypes.*;
import CustomUIElements.*;
import DataManagment.*;

public class StudentDashboard extends DashBoard{
    private Student student;

    public StudentDashboard(Student student, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        super(courseDB, userDB);
        this.student = student;

        setTitle("Dashboard - " + student.getUsername());
        navButtons.setLayout(new GridLayout(1,2, 10, 10));

        for (Progress prog: student.getAllProgressTrackers()){
            prog.updateTrackers(courseDB.getRecordByID(prog.getCourseID()));
        }
        userDB.saveToFile();

        JButton viewButton = new JButton();
        viewButton.setBackground(Color.LIGHT_GRAY);
        viewButton.setText("My Courses ");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CardScrollPane(courseDB, course -> student.getCourseIDs().contains(course.getID())){
                    @Override
                    public void leftClickHandler(MouseEvent e){
                        Component comp = e.getComponent();
                        while (!(comp instanceof Card) && comp != null){
                            comp = comp.getParent();
                        }
                        final Card clickedCard = (Card) comp;
                        if (e.getClickCount() == 2){
                            Course selectedCourse = clickedCard.getCourse();
                            changeContentPanel(new CourseViewFrame(selectedCourse, student, courseDB, userDB, StudentDashboard.this));
                        }
                    }
                });
            }
        });
        navButtons.add(viewButton);

        JButton enrollButton = new JButton();
        enrollButton.setBackground(Color.LIGHT_GRAY);
        enrollButton.setText("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CardScrollPane(courseDB, course->!student.getCourseIDs().contains(course.getID())){
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
                });
            }
        });
        navButtons.add(enrollButton);

        handleHomeButton();
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

    static void main() {
        UserDatabaseManager userDB = new UserDatabaseManager("users.json");
        new StudentDashboard((Student) userDB.getRecordByID("S0001"), new CourseDatabaseManager("courses.json"), userDB);
    }
}
