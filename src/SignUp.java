import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SignUp extends JFrame {
    private JPanel SignUp;
    private JPasswordField ConfirmedPass;
    private JTextField userName;
    private JTextField Email;
    private JPasswordField Password;
    private JComboBox<String> role;
    private JButton backButton;
    private JButton signUpButton;

    private final AuthenticateManager authManager;

    public SignUp() {
        UserDatabaseManager database = new UserDatabaseManager("users.json");
        this.authManager = new AuthenticateManager(database);
        setTitle("SignUp");
        setContentPane(SignUp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450,250);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
        userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Email.requestFocus();
                }
            }
        });
        Email.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Password.requestFocus();
                }
            }
        });
        Password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ConfirmedPass.requestFocus();
                }
            }
        });
        ConfirmedPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    role.requestFocus();
                }
            }
        });
        role.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSignUp();
                }
            }
        });
    }

    private void handleSignUp() {
        String username = userName.getText();
        String email = Email.getText();
        String password = new String(Password.getPassword());
        String confirmedPass = new String(ConfirmedPass.getPassword());
        String userRole = (String) role.getSelectedItem();

        if (!ValidationResult.isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, ValidationResult.getUsernameError(), "Invalid Username", JOptionPane.ERROR_MESSAGE);
            userName.selectAll();
            userName.requestFocus();
            return;
        }

        if (!ValidationResult.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, ValidationResult.getEmailError(), "Invalid Email", JOptionPane.ERROR_MESSAGE);
            Email.selectAll();
            Email.requestFocus();
            return;
        }

        if (!ValidationResult.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, ValidationResult.getPasswordError(),
                    "Invalid Password", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!password.equals(confirmedPass)){
            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            ConfirmedPass.setText("");
            Password.setText("");
            Password.requestFocus();
            return;
        }

        try {
            User newUser = authManager.signup(username, email, password, userRole);
            JOptionPane.showMessageDialog(this, "Account created successfully!\nWelcome, " + newUser.getUsername() + " You can now sign in with your credentials.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Login();
        } catch (IllegalArgumentException e){
            System.out.println("Status: FAILED - " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Signup Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
