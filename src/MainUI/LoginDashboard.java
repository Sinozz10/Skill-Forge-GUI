package MainUI;

import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginDashboard extends JFrame{
    private JPanel mainFramePanel;
    private JPanel contentPanel;
    private UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();

    public LoginDashboard(){
        changeContentPanel(new Login(LoginDashboard.this));

        setContentPane(mainFramePanel);
        setSize(450, 600);
        setBackground(Color.gray);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(LoginDashboard.this, "Are you sure you want to close the application?",
                        "Close Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (option == JOptionPane.YES_OPTION) {
                    userDB.saveToFile();
                    System.exit(0);
                }
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
}
