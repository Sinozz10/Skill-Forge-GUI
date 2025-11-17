import java.util.ArrayList;
import java.util.List;

public class Course implements Record{
    private final String courseID, instructorID;
    private String title , description;
    private List<Chapter> chapters = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public Course(String courseID, String title, String description, String instructorID) {
        this.courseID = courseID;
        this.title = title;
        this.description = description;
        this.instructorID = instructorID;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseID + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructorId='" + instructorID + '\'' +
                ", chapters=" + chapters +
                ", students=" + students +
                '}';
    }

    @Override
    public String getID() {
        return courseID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructorID() {
        return instructorID;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void enrollStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public void deleteLesson(Chapter chapter) {
        chapters.remove(chapter);
    }

    public Chapter getLessonById(String courseId) {
        for (Chapter chapter : chapters) {
            if (chapter.getChapterID().equals(courseId)) {
                return chapter;
            }
        }
        return null ;
    }
}