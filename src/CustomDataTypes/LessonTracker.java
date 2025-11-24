package CustomDataTypes;

import com.google.gson.annotations.*;


public class LessonTracker extends GeneralTracker{

    public LessonTracker(Lesson lesson) {
        super(lesson.getLessonID());
    }

    public LessonTracker(Lesson lesson, boolean state) {
        super(lesson.getLessonID(), state);
    }
}