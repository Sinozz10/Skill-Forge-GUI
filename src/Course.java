import java.util.ArrayList;
import java.util.List;

public class Course implements Record{
    private final String courseID, instructorID;
    private String title , description;
    private List<Chapter> chapters = new ArrayList<>();
    private List<String> studentIDs = new ArrayList<>();

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
                ", studentIDs=" + studentIDs +
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

    public List<String> getStudentIDs() {
        return studentIDs;
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

    public void setStudents(List<String> studentIDs) {
        this.studentIDs = studentIDs;
    }

    public void enrollStudent(Student student) {
        if (!studentIDs.contains(student.getID())) {
            studentIDs.add(student.getID());
        }
    }

    public void unEnrollStudent(Student student) {
        studentIDs.remove(student.getID());
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public void deleteChapter(Chapter chapter) {
        chapters.remove(chapter);
    }

    public Chapter getChapterById(String courseId) {
        for (Chapter chapter : chapters) {
            if (chapter.getChapterID().equals(courseId)) {
                return chapter;
            }
        }
        return null ;
    }
}