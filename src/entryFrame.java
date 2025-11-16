import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class entryFrame extends JFrame {
    private JPanel Entry;
    private JButton signUpButton;
    private JButton loginButton;
    private JLabel WelcomeMessage;

    public entryFrame() {
        setTitle("Skill Forge - Welcome");
        setContentPane(Entry);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               dispose();
               new LoginFrame();
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignUp();
            }
        });
    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new entryFrame());
//    }
}
