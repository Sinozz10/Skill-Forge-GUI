package DataManagment;

import CustomDataTypes.Admin;
import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomDataTypes.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class UserDatabaseManager extends JsonDatabaseManager<User> {
    private static final UserDatabaseManager userDB = new UserDatabaseManager("users.json");

    private UserDatabaseManager(String filename) {
        super(filename);
    }

    public static UserDatabaseManager getDatabaseInstance() {
        return userDB;
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
                JsonObject obj = element.getAsJsonObject();
                String role = obj.get("role").getAsString();

                User user;
                if ("student".equalsIgnoreCase(role)) {
                    user = gson.fromJson(obj, Student.class);
                } else if ("instructor".equalsIgnoreCase(role)) {
                    user = gson.fromJson(obj, Instructor.class);
                } else if ("admin".equalsIgnoreCase(role)) {
                    user = gson.fromJson(obj, Admin.class);
                } else {
                    throw new RuntimeException("Invalid role");
                }
                records.add(user);
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

            for (User user : records) {
                JsonElement element;
                if (user instanceof Student) {
                    element = gson.toJsonTree(user, Student.class);
                } else if (user instanceof Instructor) {
                    element = gson.toJsonTree(user, Instructor.class);
                } else if (user instanceof Admin) {
                    element = gson.toJsonTree(user, Admin.class);
                } else {
                    element = gson.toJsonTree(user, User.class);
                }
                jsonArray.add(element);
            }

            writer.write(gson.toJson(jsonArray));
        } catch (IOException e) {
            System.err.println("Error writing to file:" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public User getRecordByEmail(String email) {
        for (User record : records) {
            if (record.getEmail().equals(email)) {
                return record;
            }
        }
        return null;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        for (User record : records) {
            if (record.getRole().equals("student")) {
                students.add((Student) record);
            }
        }
        return students;
    }

    public ArrayList<Instructor> getInstructors() {
        ArrayList<Instructor> instructors = new ArrayList<>();
        for (User record : records) {
            if (record.getRole().equals("instructor")) {
                instructors.add((Instructor) record);
            }
        }
        return instructors;
    }

    public ArrayList<Admin> getAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        for (User record : records) {
            if (record.getRole().equals("admin")) {
                admins.add((Admin) record);
            }
        }
        return admins;
    }

    public User getRecordByUsername(String name) {
        for (User record : records) {
            if (record.getUsername().equals(name)) {
                return record;
            }
        }
        return null;
    }
}
