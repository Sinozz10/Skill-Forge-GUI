import javax.swing.*;
import java.awt.*;

public class LessonPanel extends JPanel{
    private JLabel titleLabel;
    private JPanel lPanel;
    private JCheckBox stateBox;

    public LessonPanel(String title) {
        setLayout(new BorderLayout());
        add(lPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        titleLabel.setText(title);
    }
}
