package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Course implements Record {
    @Expose
    private final String courseID, instructorID;
    @Expose
    private String title, description;
    @Expose
    private StatusCourse status;
    @Expose
    private ArrayList<Chapter> chapters = new ArrayList<>();
    @Expose
    private ArrayList<String> studentIDs = new ArrayList<>();

    public Course(String courseID, String title, String description, String instructorID) {
        this.courseID = courseID;
        this.title = title;
        this.description = description;
        this.instructorID = instructorID;
        this.status = StatusCourse.PENDING; // bybd2 dayman as Pending w hyt8yar b3daha.
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseID + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructorId='" + instructorID + '\'' +
                ", approvalStatus=" + status +
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

    public StatusCourse getStatus() {
        return status;
    } //getter lel approval

    public void setStatus(StatusCourse status) {
        this.status = status;
    } //Setters lel approval

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public ArrayList<String> getStudentIDs() {
        return studentIDs;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void setStudents(ArrayList<String> studentIDs) {
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
        return null;
    }
}