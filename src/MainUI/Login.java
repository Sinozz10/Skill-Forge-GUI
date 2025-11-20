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
    private JPanel login;
    private JPasswordField passWord;
    private JTextField userName;
    private JButton loginButton;
    private JButton signupButton;
    private LoginDashboard ld;

    private final AuthenticateManager auth;

    public Login(LoginDashboard ld) {
        this.ld = ld;
        UserDatabaseManager database = new UserDatabaseManager("users.json");
        this.auth= new AuthenticateManager(database);
        setLayout(new BorderLayout());
        add(login,BorderLayout.CENTER);

        passWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {}
        });
        passWord.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        userName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {}
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    passWord.requestFocus();
                }
            }
        });
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ld.changeContentPanel(new SignUp(ld));
            }
        });
        loginButton.setSize(new Dimension(10,20));
        signupButton.setSize(new Dimension(10,20));

        loginButton.setBackground(Color.lightGray);
        signupButton.setBackground(Color.lightGray);
    }

    private void handleLogin() {
        String userName = this.userName.getText().trim();
        String password = new String(passWord.getPassword());
        if (userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both userName and password!", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = auth.login(userName, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful!\nWelcome, " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);

                CourseDatabaseManager courseDB = new CourseDatabaseManager("courses.json");
                UserDatabaseManager userDB = auth.database;

                if (user.getRole().equalsIgnoreCase("student")) {
                    new StudentDashboard((Student) userDB.getRecordByID(user.getID()), courseDB, userDB);
                } else if (user.getRole().equalsIgnoreCase("instructor")) {
                    new InstructorDashboard((Instructor) userDB.getRecordByID(user.getID()), courseDB, userDB);
                }
                ld.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid userName or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passWord.setText("");
                passWord.requestFocus();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred during login. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
