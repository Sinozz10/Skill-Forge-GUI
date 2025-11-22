package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Instructor;
import CustomUIElements.Card;
import CustomUIElements.CardScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import CustomDataTypes.*;
import CustomUIElements.*;
import DataManagment.*;
import com.formdev.flatlaf.FlatDarculaLaf;

public class InstructorDashboard extends DashBoard{
    private final Instructor instructor;

    public InstructorDashboard(Instructor instructor) {
        super();
        this.instructor = instructor;

        setTitle("Dashboard - " + instructor.getUsername());
        setBackground(Color.GRAY);
        navButtons.setLayout(new GridLayout(1,5, 10, 10));
        setResizable(false);

        JButton viewCoursesButton = new JButton("My Courses");
        viewCoursesButton.setForeground(Color.BLACK);
        viewCoursesButton.setBackground(Color.LIGHT_GRAY);
        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewCourses();
            }
        });
        navButtons.add(viewCoursesButton);

        JButton viewStudentsButton = new JButton("My Students");
        viewStudentsButton.setForeground(Color.BLACK);
        viewStudentsButton.setBackground(Color.LIGHT_GRAY);
        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new ViewStudents(instructor));
            }
        });
        navButtons.add(viewStudentsButton);

        handleHomeButton();
    }


    public void handleViewCourses(){
        JButton addButton = new JButton("Add Course");
        addButton.setForeground(Color.BLACK);
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CourseAdd(instructor));
            }
        });

        CardScrollPane pane = new CardScrollPane( null, course -> instructor.getID().equals(course.getInstructorID())) {
            @Override
            public void rightClickHandler(MouseEvent e){
                Component comp = e.getComponent();
                while (!(comp instanceof Card) && comp != null){
                    comp = comp.getParent();
                }
                final Card clickedCard = (Card) comp;

                // pop up menu
                final JPopupMenu popupMenu = new JPopupMenu();

                //delete item
                JMenuItem deleteItem = new JMenuItem("Delete");
                deleteItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirm = JOptionPane.showConfirmDialog(InstructorDashboard.this,
                                "Are you sure you want to delete?",
                                "Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            assert clickedCard != null;
                            handleDelete(instructor, clickedCard.getCourse());
                            loadCoursesFromDatabase();
                        }

                    }
                });
                popupMenu.add(deleteItem);

                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }

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
