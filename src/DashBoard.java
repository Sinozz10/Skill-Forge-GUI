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

    protected SuperUser currUser;

    public DashBoard() {
        setTitle("Dashboard");
        setContentPane(dashPanel);
        setSize(750, 400);
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
            System.out.println("USER LOGOUT");
            System.out.println("===========================================");
            System.out.println("User: " + currUser.getUsername());
            System.out.println("Role: " + currUser.getRole().toUpperCase());
            System.out.println("Time: " + java.time.LocalDateTime.now());
            System.out.println("===========================================\n");
            this.dispose();
            new entryFrame();
        } else {
            System.out.println("Logout cancelled");
        }
    }

}
