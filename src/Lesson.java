package src;

import java.util.ArrayList;
import java.util.List;

public class Lesson {

    private final String lessonId,title ,content ;
    private List<String> resources = new ArrayList<>();

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getLessonId() {
        return lessonId;
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


}
