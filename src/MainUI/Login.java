package MainUI;

import CustomDataTypes.Admin;
import CustomDataTypes.Instructor;
import CustomDataTypes.Student;
import CustomDataTypes.User;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JPanel {
    private final JPasswordField passWord;
    private final JTextField userName;
    private final LoginDashboard ld;
    private final AuthenticateManager auth;

    public Login(LoginDashboard ld) {
        this.ld = ld;
        this.auth = new AuthenticateManager();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleSystem = new JLabel("SKILL FORGE SYSTEM");
        titleSystem.setForeground(Color.WHITE);
        titleSystem.setFont(new Font("Arial", Font.BOLD, 38));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 30, 5);
        add(titleSystem, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel userNAME = new JLabel("Username: ");
        userNAME.setFont(new Font("Arial", Font.PLAIN, 14));
        add(userNAME, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        userName = new JTextField(20);
        userName.setPreferredSize(new Dimension(200, 25));
        add(userName, gbc);


        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passWORD = new JLabel("Password: ");
        passWORD.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passWORD, gbc);


        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        passWord = new JPasswordField(20);
        passWord.setPreferredSize(new Dimension(200, 25));
        add(passWord, gbc);


        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(120, 35));
        loginBtn.setForeground(Color.black);
        loginBtn.setBackground(Color.LIGHT_GRAY);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setMargin(new Insets(3, 10, 3, 10));
        add(loginBtn, gbc);

        // mawdoo el not a member
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 5, 5, 5);
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel notMemberLabel = new JLabel("Not a Member ?");
        notMemberLabel.setForeground(Color.BLACK);
        notMemberLabel.setBackground(Color.LIGHT_GRAY);
        notMemberLabel.setFont(new Font("Veranda", Font.BOLD, 14));

        JButton signupBtn = new JButton("Signup");
        signupBtn.setPreferredSize(new Dimension(100, 30));
        signupBtn.setForeground(Color.black);
        signupBtn.setBackground(Color.LIGHT_GRAY);
        signupBtn.setFont(new Font("Arial", Font.BOLD, 14));
        signupBtn.setMargin(new Insets(3, 10, 3, 10));
        signupPanel.add(notMemberLabel);
        signupPanel.add(signupBtn);
        add(signupPanel, gbc);


        //kol el listeners b-listeno lel enter.
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

        loginBtn.addActionListener(_ -> handleLogin());
        signupBtn.addActionListener(_ -> ld.changeContentPanel(new SignUp(ld)));
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
                } else if (user.getRole().equalsIgnoreCase("admin")) {
                    new AdminDashboard((Admin) userDB.getRecordByID(user.getID()));
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