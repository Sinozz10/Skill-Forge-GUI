import DataManagment.UserDatabaseManager;
import MainUI.LoginDashboard;

void main() {
    UserDatabaseManager userDB = new UserDatabaseManager("users.json");
    new LoginDashboard(userDB);
}
