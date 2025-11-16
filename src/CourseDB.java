package src;

import java.util.ArrayList;
import java.util.List;

public class CourseDB {



    private List<Course> allCourses = new ArrayList<>();

    public void updateCourse(Course updated) {
        for (int i = 0; i <allCourses.size(); i++) {
            if (allCourses.get(i).getCourseId().equals(updated.getCourseId())) {
                allCourses.set(i, updated);
                return;  }
            }
    }

    public void deleteCourse(String courseId) {
        for (int i = 0; i < allCourses.size(); i++) {
            if (allCourses.get(i).getCourseId().equals(courseId)) {
                allCourses.remove(i);
            }}
    }

    public Course courseById(String courseId) {
        for (int i = 0; i < allCourses.size(); i++) {
            if (allCourses.get(i).getCourseId().equals(courseId)) {
                return allCourses.get(i);
            }
        }
        return null;
    }
    public List<String> getStudentsByInstructor(String instructorId) {
        List<String> allStudents = new ArrayList<>();

        for (int i = 0; i < allStudents.size(); i++) {
            if (allCourses.get(i).getInstructorId().equals(instructorId)) {
                allStudents.addAll(allCourses.get(i).getStudents());}
            }
        return allStudents;
    }

    public List<Lesson> fetchLessonsByCourse(String courseId) {
        return courseById(courseId).getLessons();
    }



}
