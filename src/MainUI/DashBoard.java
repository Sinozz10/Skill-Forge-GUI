package MainUI;

import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class DashBoard extends JFrame{
    private JButton logoutButton;
    private JPanel navBar;
    private JPanel contentPanel;
    private JPanel dashPanel;
    protected JPanel navButtons;
    private JButton homeButton;
    protected final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    protected final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();

    public DashBoard() {
        setContentPane(dashPanel);
        setSize(900, 550);
        setBackground(Color.gray);

        logoutButton.setBackground(Color.LIGHT_GRAY);
        logoutButton.setForeground(Color.BLACK);
        homeButton.setBackground(Color.LIGHT_GRAY);
        homeButton.setForeground(Color.BLACK);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(DashBoard.this, "Are you sure you want to close the application?",
                        "Close Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (option == JOptionPane.YES_OPTION) {
                    userDB.saveToFile();
                    courseDB.saveToFile();
                    System.exit(0);
                }
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        navBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        logoutButton.addActionListener(_ -> handleLogout());

        homeButton.addActionListener(_ -> handleHomeButton());
    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            userDB.saveToFile();
            courseDB.saveToFile();
            this.dispose();
            new LoginDashboard();
        }
    }

    abstract void handleHomeButton();
}
