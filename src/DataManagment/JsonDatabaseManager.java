package DataManagment;

import CustomDataTypes.Record;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public abstract class JsonDatabaseManager<T extends Record> {
    protected final String filename;
    protected final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
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

    public void deleteRecord(String recordID) {
        records.removeIf(record -> recordID.equals(record.getID()));
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

    public abstract void saveToFile();

    public abstract void readFromFile();

    public String getFilename() {
        return filename;
    }
}
