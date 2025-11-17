import java.util.ArrayList;

public class Chapter {
    private ArrayList<Lesson> lessons = new ArrayList<>();
    private final String chapterID, courseID;
    private int order;

    public Chapter(String chapterID, String courseID, int order) {
        this.chapterID = chapterID;
        this.courseID = courseID;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getChapterID() {
        return chapterID;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
