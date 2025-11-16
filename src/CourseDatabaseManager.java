import java.util.ArrayList;
import java.util.List;

public class CourseDatabaseManager {
    private List<Course> allCourses = new ArrayList<>();

    public void updateCourse(Course updated) {
        for (int i = 0; i <allCourses.size(); i++) {
            if (allCourses.get(i).getCourseId().equals(updated.getCourseId())) {
                allCourses.set(i, updated);
                return;
            }
        }
    }

    public void deleteCourse(String courseId) {
        for (Course course: allCourses){
            if (course.getCourseId().equals(courseId)){
                allCourses.remove(course);
            }
        }
    }

    public Course courseById(String courseId) {
        for (Course course: allCourses){
            if (course.getCourseId().equals(courseId)){
                return course;
            }
        }
        return null;
    }
    public boolean findCourse(String courseId) {
        return courseId != null;
    }
}
