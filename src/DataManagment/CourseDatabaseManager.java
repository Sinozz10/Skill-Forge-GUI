package DataManagment;

import CustomDataTypes.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CourseDatabaseManager extends JsonDatabaseManager<Course> {
    private static final CourseDatabaseManager courseDB = new CourseDatabaseManager("courses.json");

    public CourseDatabaseManager(String filename) {
        super(filename);
    }

    public static CourseDatabaseManager getDatabaseInstance() {
        return courseDB;
    }

    @Override
    public void readFromFile() {
        records.clear();
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (FileReader reader = new FileReader(filename)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject courseObj = element.getAsJsonObject();

                Course course = gson.fromJson(courseObj, Course.class);

                if (course.getChapters() != null && courseObj.has("chapters")) {
                    JsonArray chaptersArray = courseObj.getAsJsonArray("chapters");

                    for (int chapterIndex = 0; chapterIndex < course.getChapters().size(); chapterIndex++) {
                        Chapter chapter = course.getChapters().get(chapterIndex);
                        JsonObject chapterObj = chaptersArray.get(chapterIndex).getAsJsonObject();

                        if (chapter.getLessons() != null && chapterObj.has("lessons")) {
                            JsonArray lessonsArray = chapterObj.getAsJsonArray("lessons");

                            for (int lessonIndex = 0; lessonIndex < chapter.getLessons().size(); lessonIndex++) {
                                Lesson lesson = chapter.getLessons().get(lessonIndex);
                                JsonObject lessonObj = lessonsArray.get(lessonIndex).getAsJsonObject();

                                if (lesson.hasQuiz() && lesson.getQuiz() != null &&
                                        lesson.getQuiz().getQuestions() != null &&
                                        lessonObj.has("quiz")) {

                                    JsonObject quizObj = lessonObj.getAsJsonObject("quiz");

                                    if (quizObj.has("questions")) {
                                        JsonArray questionsArray = quizObj.getAsJsonArray("questions");

                                        for (int questionIndex = 0; questionIndex < lesson.getQuiz().getQuestions().size(); questionIndex++) {
                                            if (questionIndex < questionsArray.size()) {
                                                JsonObject questionObj = questionsArray.get(questionIndex).getAsJsonObject();
                                                String type = questionObj.get("type").getAsString();

                                                Question correctQuestion;
                                                if ("TEXT_QUESTION".equals(type)) {
                                                    correctQuestion = gson.fromJson(questionObj, TextQuestion.class);
                                                } else if ("CHOICE_QUESTION".equals(type)) {
                                                    correctQuestion = gson.fromJson(questionObj, ChoiceQuestion.class);
                                                } else {
                                                    continue;
                                                }

                                                lesson.getQuiz().getQuestions().set(questionIndex, correctQuestion);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                records.add(course);
            }
        } catch (IOException e) {
            System.err.println("Error file not found: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void saveToFile() {
        try (FileWriter writer = new FileWriter(filename)) {
            JsonArray jsonArray = new JsonArray();

            for (Course course : records) {
                JsonElement element = gson.toJsonTree(course, Course.class);
                jsonArray.add(element);
            }

            writer.write(gson.toJson(jsonArray));
        } catch (IOException e) {
            System.err.println("Error writing to file:" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
