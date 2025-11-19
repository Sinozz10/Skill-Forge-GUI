package DataManagment;

import CustomDataTypes.Course;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CourseDatabaseManager extends JsonDatabaseManager<Course>{
    public CourseDatabaseManager(String filename) {
        super(filename);
    }

    public void readFromFile() {
        records.clear();
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (FileReader reader = new FileReader(filename)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();

                Course course = gson.fromJson(obj, Course.class);
                records.add(course);
            }
        } catch (IOException e) {
            System.err.println("Error file not found: "+e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void saveToFile(){
        try(FileWriter writer = new FileWriter(filename)){
            JsonArray jsonArray = new JsonArray();

            for (Course course : records) {
                JsonElement element = gson.toJsonTree(course, Course.class);
                jsonArray.add(element);
            }

            writer.write(gson.toJson(jsonArray));
        } catch (IOException e){
            System.err.println("Error writing to file:" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
