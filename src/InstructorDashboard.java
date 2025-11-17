import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class InstructorDashboard extends DashBoard{

    public InstructorDashboard(Instructor instructor, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        super(courseDB, userDB);
        setTitle("Dashboard - " + instructor.getUsername());
        navButtons.setLayout(new GridLayout(1,3, 10, 10));

        JButton addButton = new JButton();
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.setText("Add Course");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new CourseAdd(courseDB, userDB, instructor));
            }
        });
        navButtons.add(addButton);

        JButton viewCoursesButton = new JButton();
        viewCoursesButton.setBackground(Color.LIGHT_GRAY);
        viewCoursesButton.setText("My Courses");
        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    changeContentPanel(new CardScrollPane(courseDB, course -> instructor.getID().equals(course.getInstructorID())) {
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
                            editItem.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    changeContentPanel(new CourseEdit(courseDB));
                                }
                            });
                            popupMenu.add(editItem);

                            //delete item
                            JMenuItem deleteItem = new JMenuItem("Delete");
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
                    });
            }
        });
        navButtons.add(viewCoursesButton);

        JButton viewStudentsButton = new JButton();
        viewStudentsButton.setBackground(Color.LIGHT_GRAY);
        viewStudentsButton.setText("My Students");
        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContentPanel(new ViewStudents(courseDB, userDB, instructor));
            }
        });
        navButtons.add(viewStudentsButton);
    }

    private void handleDelete(Instructor instructor, Course course) {
        String id = course.getID();
        Course courseToDelete = courseDB.getRecordByID(id);

        courseDB.deleteRecord(id);
        courseDB.saveToFile();

        instructor.removeCourse(courseToDelete);
        userDB.updateRecord(instructor);
        userDB.saveToFile();

        JOptionPane.showMessageDialog(InstructorDashboard.this, "Course deleted", "Success", JOptionPane.WARNING_MESSAGE);
    }

    static void main() {
        UserDatabaseManager userDB = new UserDatabaseManager("users.json");
        new InstructorDashboard((Instructor) userDB.getRecordByID("I0001"), new CourseDatabaseManager("courses.json"), userDB);
    }
}
