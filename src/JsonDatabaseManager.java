import com.google.gson.Gson;

import java.util.ArrayList;

public abstract class JsonDatabaseManager<T extends Record> {
    public JsonDatabaseManager(String filename){
        FilesChecker.checkFile(filename);
    }

    protected final ArrayList<T> records = new ArrayList<>();

    public ArrayList<T> getRecords() {
        return records;
    }

    public void updateRecord(T updated) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getID().equals(updated.getID())) {
                records.set(i, updated);
                return;
            }
        }
    }

    public void deleteCourse(String recordID) {
        records.removeIf(records -> records.getID().equals(recordID));
    }

    public T getRecordByID(String recordID) {
        for (T records: records){
            if (records.getID().equals(recordID)){
                return records;
            }
        }
        return null;
    }

    public boolean findCourse(String recordID) {
        return getRecordByID(recordID) != null;
    }

    public void saveToFile(){

    }

    public void readFromFile(){

    }

    static void main() {
        Gson gson = new Gson();
        Lesson lesson = new Lesson("id", "title", "content");
        Tracker tracker = new Tracker(lesson);
        System.out.println(gson.toJson(tracker));
    }
}
