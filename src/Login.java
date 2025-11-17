import javax.swing.*;
import java.awt.event.*;

public class Login extends JFrame {
    private JPanel login;
    private JPasswordField passWord;
    private JTextField userName;
    private JButton loginButton;
    private JButton signupButton;

    private final AuthenticateManager auth;

    public Login() {
        UserDatabaseManager database = new UserDatabaseManager("users.json");
        this.auth= new AuthenticateManager(database);
        setTitle("Login");
        setContentPane(login);
        setSize(280, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

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
                dispose();
                new SignUp();
            }
        });
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
                this.dispose();

                CourseDatabaseManager courseDB = new CourseDatabaseManager("courses.json");
                UserDatabaseManager userDB = auth.database;

                if (user.getRole().equalsIgnoreCase("student")) {
                    new StudentDashboard(new Student(user.getID(), user.getRole(), user.getUsername(), user.getEmail(), user.getHashedPassword()), courseDB, userDB);
                } else if (user.getRole().equalsIgnoreCase("instructor")) {

                    new InstructorDashboard(new Instructor(user.getID(), user.getRole(), user.getUsername(), user.getEmail(), user.getHashedPassword()), courseDB, userDB);
                }

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
