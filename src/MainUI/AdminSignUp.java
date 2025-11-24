package MainUI;

public class AdminSignUp extends SignUp {
    public AdminSignUp(LoginDashboard dashboard) {
        super(dashboard);
        backButton.setVisible(false);
        role.setVisible(false);
        roleLabel.setVisible(false);
    }

    @Override
    protected void getInputs() {
        username = userName.getText();
        email = Email.getText();
        password = new String(Password.getPassword());
        confirmedPass = new String(ConfirmedPass.getPassword());
        userRole = "admin";
    }
}
