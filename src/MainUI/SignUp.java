package MainUI;

import CustomDataTypes.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SignUp extends JPanel {
    //Attributes
    protected JPanel SignUp;
    protected JPasswordField ConfirmedPass;
    protected JTextField userName;
    protected JTextField Email;
    protected JPasswordField Password;
    protected JComboBox<String> role;
    protected JButton backButton;
    protected JButton signUpButton;
    protected JLabel roleLabel;

    protected final LoginDashboard dashBoard;
    protected final AuthenticateManager authManager;

    protected String username;
    protected String email;
    protected String password;
    protected String confirmedPass;
    protected String userRole;

    //Constructor
    public SignUp(LoginDashboard dashBoard) {
        this.dashBoard = dashBoard;
        this.authManager = new AuthenticateManager();
        setLayout(new BorderLayout());
        add(SignUp,BorderLayout.CENTER);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(Color.LIGHT_GRAY);

        backButton.addActionListener(_ -> dashBoard.changeContentPanel(new Login(dashBoard)));
        signUpButton.addActionListener(_ -> handleSignUp());
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
                    if (role.isVisible()) {
                        role.requestFocus();
                    }else {
                        handleSignUp();
                    }
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

        signUpButton.setPreferredSize(new Dimension(10,20));
        signUpButton.setMargin(new Insets(3, 10, 3, 10));
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setBackground(Color.LIGHT_GRAY);
        signUpButton.setForeground(Color.BLACK);
    }

    protected void getInputs(){
        username = userName.getText().trim();
        email = Email.getText().trim();
        password = new String(Password.getPassword());
        confirmedPass = new String(ConfirmedPass.getPassword());
        userRole = (String) role.getSelectedItem();
    }

    private void handleSignUp() {
        getInputs();

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
            dashBoard.changeContentPanel(new Login(dashBoard));
        } catch (IllegalArgumentException e){
            System.out.println("Status: FAILED - " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Signup Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
