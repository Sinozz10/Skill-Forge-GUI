import javax.swing.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JPanel login;
    private JPasswordField passWord;
    private JTextField userName;
    private JButton loginButton;
    private JButton backButton;

    private final AuthenticateManager auth;

    public LoginFrame() {
        UserDatabaseManager database = new UserDatabaseManager("users.json");
        this.auth= new AuthenticateManager(database);
        setTitle("Login");
        setContentPane(login);
        setSize(500, 200);
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //new entryFrame();
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
    }

    private void handleLogin() {
        String userName = this.userName.getText().trim();
        String password = new String(passWord.getPassword());

        System.out.println("==========================");
        System.out.println("Username: " + userName);
        System.out.println("Time: " + java.time.LocalDateTime.now());

        if (userName.isEmpty() || password.isEmpty()) {
            System.out.println("Status: FAILED - Empty credentials");
            JOptionPane.showMessageDialog(this, "Please enter both userName and password!", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = auth.login(userName, password);
            if (user != null) {
                System.out.println("Status: SUCCESS");
                System.out.println("User ID: " + user.getID());
                System.out.println("Username: " + user.getUsername());
                System.out.println("Role: " + user.getRole().toUpperCase());
                System.out.println("===========================================\n");

                if (user.getRole().equals("student")) {
                    System.out.println(" STUDENT LOGIN DETECTED");
                    System.out.println("   Name: " + user.getUsername());
                    System.out.println("   Email: " + user.getEmail());
                    System.out.println("   Opening Student Dashboard...\n");
                } else if (user.getRole().equals("instructor")) {
                    System.out.println(" INSTRUCTOR LOGIN DETECTED");
                    System.out.println(" Name: " + user.getUsername());
                    System.out.println(" Email: " + user.getEmail());
                    System.out.println("   Opening Instructor Dashboard...\n");
                }

                JOptionPane.showMessageDialog(this, "Login successful!\nWelcome, " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

                if (user.getRole().equalsIgnoreCase("student")) {
                    new StudentDashboard((Student) user);
                } else if (user.getRole().equalsIgnoreCase("instructor")) {
                    new InstructorDashboard((Instructor) user);
                }

            } else {
                System.out.println(" FAILED - Invalid credentials- Wrong userName or password");
                JOptionPane.showMessageDialog(this, "Invalid userName or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passWord.setText("");
                passWord.requestFocus();
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Status: FAILED - Validation error");
            System.out.println("Error: " + e.getMessage());
            System.out.println("===========================================\n");
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            System.out.println("Status: FAILED - System error");
            JOptionPane.showMessageDialog(this, "An error occurred during login. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
