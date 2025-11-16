import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

public class UserDatabaseManager extends JsonDatabaseManager<User>{
    Type listType = null;
    public UserDatabaseManager(String filename) {
        super(filename);
    }

    @Override
    public void readFromFile() {
        records.clear();
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        if (listType == null) {
            listType = new TypeToken<ArrayList<User>>(){}.getType();
        }
        try (FileReader reader = new FileReader(filename)) {
            ArrayList<User> list = gson.fromJson(reader, listType);
            if (list != null) {
                records = list;
            }
        } catch (IOException e) {
            System.err.println("Error file not found: "+e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public User getRecordByEmail(String email) {
        for(User record: records){
            if (record.getEmail().equals(email)){
                return record;
            }
        }
        return null;
    }
}
