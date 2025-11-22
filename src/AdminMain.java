import MainUI.AdminSignUp;
import com.formdev.flatlaf.FlatDarculaLaf;

void main() {
    FlatDarculaLaf.setup();
    AdminSignUp frame = new AdminSignUp();
    frame.setTitle("Create Admin Account");
    frame.setSize(400, 330);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
}