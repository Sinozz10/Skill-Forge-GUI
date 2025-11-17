import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;

public abstract class JsonDatabaseManager<T extends Record> {
    protected final String filename;
    protected final Gson gson = new Gson();
    protected ArrayList<T> records = new ArrayList<>();

    public JsonDatabaseManager(String filename){
        FilesChecker.checkFile(filename);
        this.filename = FilesChecker.getPath(filename);
        readFromFile();
    }

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

    public void addRecord(T newRecord) {
        records.add(newRecord);
    }

    public void deleteCourse(String recordID) {
        records.removeIf(records -> records.getID().equals(recordID));
    }

    public T getRecordByID(String recordID) {
        for (T record: records){
            if (recordID.equals(record.getID())){
                return record;
            }
        }
        return null;
    }

    public boolean findRecord(String recordID) {
        return getRecordByID(recordID) != null;
    }

    public void saveToFile(){
        try(FileWriter writer = new FileWriter(filename)){
            writer.write(gson.toJson(records));
        }catch (IOException e){
            System.err.println("Error writing to file:" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public abstract void readFromFile();

    public String getFilename() {
        return filename;
    }
}
