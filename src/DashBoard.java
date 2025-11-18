import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashBoard extends JFrame{
    private JButton logoutButton;
    private JPanel navBar;
    private JPanel contentPanel;
    private JPanel dashPanel;
    protected JPanel navButtons;
    private JButton homeButton;
    protected final CourseDatabaseManager courseDB;
    protected final UserDatabaseManager userDB;

    public DashBoard(CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.courseDB = courseDB;
        this.userDB = userDB;
        setContentPane(dashPanel);
        setSize(900, 450);
        setVisible(true);

        logoutButton.setBackground(Color.LIGHT_GRAY);
        homeButton.setBackground(Color.LIGHT_GRAY);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        navBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
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
            this.dispose();
            new Login();
        }
    }
}
