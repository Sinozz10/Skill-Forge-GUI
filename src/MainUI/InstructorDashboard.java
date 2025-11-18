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

import CustomDataTypes.*;
import CustomUIElements.*;
import DataManagment.*;

public class InstructorDashboard extends DashBoard{
    private Instructor instructor;

    public InstructorDashboard(Instructor instructor, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        super(courseDB, userDB);
        this.instructor = instructor;

        setTitle("Dashboard - " + instructor.getUsername());
        navButtons.setLayout(new GridLayout(1,5, 10, 10));

        JButton addButton = new JButton("Add Course");
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CourseAdd(courseDB, userDB, instructor));
            }
        });

        JButton viewCoursesButton = new JButton("My Courses");
        viewCoursesButton.setBackground(Color.LIGHT_GRAY);
        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardScrollPane pane = new CardScrollPane(courseDB, course -> instructor.getID().equals(course.getInstructorID())) {
                    @Override
                    public void rightClickHandler(MouseEvent e){
                        Component comp = e.getComponent();
                        while (!(comp instanceof Card) && comp != null){
                            comp = comp.getParent();
                        }
                        final Card clickedCard = (Card) comp;

                        // pop up menu
                        final JPopupMenu popupMenu = new JPopupMenu();

                        // edit item
                        JMenuItem editItem = new JMenuItem("Edit");
                        editItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        editItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                changeContentPanel(new CourseEdit(courseDB, clickedCard.getCourse()));
                            }
                        });
                        popupMenu.add(editItem);

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
                                    handleDelete(instructor, clickedCard.getCourse());
                                    loadCoursesFromDatabase();
                                }

                            }
                        });
                        popupMenu.add(deleteItem);

                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
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
        });
        navButtons.add(viewCoursesButton);

        JButton viewStudentsButton = new JButton("My Students");
        viewStudentsButton.setBackground(Color.LIGHT_GRAY);
        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new ViewStudents(courseDB, userDB, instructor));
            }
        });
        navButtons.add(viewStudentsButton);

        // Row 2 - Chapter & Lesson Management
        JButton chapterButton = new JButton("Manage Chapters");
        chapterButton.setBackground(Color.LIGHT_GRAY);
        chapterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new ChapterManager(courseDB,userDB));
            }
        });
        navButtons.add(chapterButton);


        JButton lessonButton = new JButton("Manage Lessons");
        lessonButton.setBackground(Color.LIGHT_GRAY);
        lessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new LessonManager(courseDB,userDB));
            }
        });
        navButtons.add(lessonButton);

        handleHomeButton();
    }


    private void handleDelete(Instructor instructor, Course course) {
        String id = course.getID();
        Course courseToDelete = courseDB.getRecordByID(id);

        courseDB.deleteRecord(id);
        courseDB.saveToFile();

        instructor.removeCourse(courseToDelete);
        userDB.updateRecord(instructor);
        userDB.saveToFile();

        JOptionPane.showMessageDialog(InstructorDashboard.this, "CustomDataTypes.Course deleted", "Success", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    void handleHomeButton(){
        JLabel userLabel = new JLabel("Welcome "+instructor.getUsername());
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        userLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JPanel userPanel = new JPanel();
        userPanel.add(userLabel);
        changeContentPanel(userPanel);
    }

    static void main() {
        UserDatabaseManager userDB = new UserDatabaseManager("users.json");
        new InstructorDashboard((Instructor) userDB.getRecordByID("I0001"), new CourseDatabaseManager("courses.json"), userDB);
    }
}
