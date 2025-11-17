import java.util.ArrayList;
import java.util.List;

public class Lesson {

    private final String lessonID;
    private String chapterID, title, content;
    private List<String> resources = new ArrayList<>();
    private int order;

    public Lesson(String lessonID, String chapterID, String title, String content, int order) {
        this.chapterID = chapterID;
        this.lessonID = lessonID;
        this.title = title;
        this.content = content;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public String getContent() {
        return content;
    }

    public String getLessonID() {
        return lessonID;
    }

    public String getChapterID() {
        return chapterID;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }
}
