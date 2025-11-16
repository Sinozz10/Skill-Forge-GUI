public class UserDatabaseManager extends JsonDatabaseManager<User>{

    public UserDatabaseManager(String filename) {
        super(filename);
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
