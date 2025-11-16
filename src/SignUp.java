import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUp extends JFrame {
    private JPanel SignUp;
    private JPasswordField ConfirmedPass;
    private JTextField userName;
    private JTextField Email;
    private JPasswordField Password;
    private JComboBox<String> role;
    private JButton backButton;
    private JButton signUpButton;

    private JsonDatabaseManager database;
    private AuthenticateManager authManager;

    public SignUp() {
        this.database = new JsonDatabaseManager();
        this.authManager = new AuthenticateManager(database);
        setTitle("SignUp");
        setContentPane(SignUp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        userName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Email.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        role.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        ConfirmedPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new entryFrame();
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
    }

    private void handleSignUp() {
        String username = userName.getText();
        String email = Email.getText();
        String password = new String(Password.getPassword());
        String confirmedPass = new String(ConfirmedPass.getPassword());
        String userRole = (String) role.getSelectedItem();

        if(!password.equals(confirmedPass)){
            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            ConfirmedPass.setText("");
            Password.setText("");
            Password.requestFocus();
            return;
        }

        try {
            User newUser = authManager.signup(username, email, password, userRole);
            System.out.println("Status: SUCCESS");
            System.out.println("User ID: " + newUser.getuID());

            if (userRole.equalsIgnoreCase("student")) {
                System.out.println(" NEW STUDENT REGISTERED");
                System.out.println(" Name: " + newUser.getUsername());
                System.out.println(" Email: " + newUser.getEmail());
                System.out.println(" ID: " + newUser.getuID());
            } else if (userRole.equalsIgnoreCase("instructor")) {
                System.out.println(" NEW INSTRUCTOR REGISTERED");
                System.out.println(" Name: " + newUser);
                System.out.println(" Email: " + newUser);
                System.out.println(" ID: " + newUser);
            }

            JOptionPane.showMessageDialog(this, "Account created successfully!\nWelcome, " + newUser.getUsername() + "You can now sign in with your credentials.", "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new LoginFrame();
        } catch (IllegalArgumentException e){
            System.out.println("Status: FAILED - " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Signup Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
