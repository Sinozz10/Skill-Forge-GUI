package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Chapter {
    @Expose
    private ArrayList<Lesson> lessons = new ArrayList<>();
    @Expose
    private final String chapterID, courseID;
    @Expose
    private int order;
    @Expose
    private String title;

    public Chapter(String chapterID, String courseID, String title, int order) {
        this.chapterID = chapterID;
        this.courseID = courseID;
        this.order = order;
        this.title = title;
    }

    public String getTitle() {
        return title;
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

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
