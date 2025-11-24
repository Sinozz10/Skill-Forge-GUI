import MainUI.AdminSignUp;
import MainUI.LoginDashboard;
import com.formdev.flatlaf.FlatDarculaLaf;

void main() {
    FlatDarculaLaf.setup();
    LoginDashboard dashboard = new LoginDashboard();
    dashboard.setVisible(false);
    dashboard.changeContentPanel(new AdminSignUp(dashboard));
    dashboard.setVisible(true);
}