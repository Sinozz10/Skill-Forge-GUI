package MainUI;

import CustomDataTypes.Course;
import CustomDataTypes.Student;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;
import DataManagment.UserDatabaseManager;

import javax.swing.*;

public class StudentCourseView extends JPanel{
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    private final JTextPane content = new JTextPane();
    private boolean quizViewState;
    private final StudentDashboard dashboard;
    private final GenerationID idGenerator;
    private final Student student;
    private final Course course;

    public StudentCourseView(Course course, Student student, StudentDashboard dashboard) {
        this.dashboard = dashboard;
        this.student = student;
        this.course = course;
        this.idGenerator = new GenerationID();

    }
}
