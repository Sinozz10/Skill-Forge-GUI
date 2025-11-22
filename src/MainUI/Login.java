package MainUI;

import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomDataTypes.User;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JPanel {
    private JPasswordField passWord;
    private JTextField userName;
    private JButton loginButton;
    private JButton signupButton;
    private LoginDashboard ld;
    private final AuthenticateManager auth;

    public Login(LoginDashboard ld) {
        this.ld = ld;
        this.auth = new AuthenticateManager();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("SKILL FORGE SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 30, 5);
        add(titleLabel, gbc);

        // Username Label
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel userLabel = new JLabel("Username: ");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(userLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        userName = new JTextField(20);
        userName.setPreferredSize(new Dimension(200, 25));
        add(userName, gbc);

        // Password Label
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        passWord = new JPasswordField(20);
        passWord.setPreferredSize(new Dimension(200, 25));
        add(passWord, gbc);

        // Login Button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.setForeground(Color.black);
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setMargin(new Insets(3, 10, 3, 10));
        add(loginButton, gbc);

        // Panel
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 5, 5, 5);
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel notMemberLabel = new JLabel("Not a Member?");
        notMemberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        signupButton = new JButton("Signup");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.setForeground(Color.black);
        signupButton.setBackground(Color.LIGHT_GRAY);
        signupButton.setFont(new Font("Arial", Font.BOLD, 14));
        signupButton.setMargin(new Insets(3, 10, 3, 10));
        signupPanel.add(notMemberLabel);
        signupPanel.add(signupButton);
        add(signupPanel, gbc);

        // Event Listeners
        userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passWord.requestFocus();
                }
            }
        });

        passWord.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ld.changeContentPanel(new SignUp(ld));
            }
        });
    }

    private void handleLogin() {
        String userName = this.userName.getText().trim();
        String password = new String(passWord.getPassword());

        if (userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = auth.login(userName, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful!\nWelcome, " + user.getUsername(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                CourseDatabaseManager courseDB = new CourseDatabaseManager("courses.json");
                UserDatabaseManager userDB = auth.database;

                if (user.getRole().equalsIgnoreCase("student")) {
                    new StudentDashboard((Student) userDB.getRecordByID(user.getID()));
                } else if (user.getRole().equalsIgnoreCase("instructor")) {
                    new InstructorDashboard((Instructor) userDB.getRecordByID(user.getID()));
                }
                ld.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passWord.setText("");
                passWord.requestFocus();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred during login. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}