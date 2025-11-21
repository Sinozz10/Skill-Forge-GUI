package CustomUIElements;

import CustomDataTypes.Lesson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LessonPanel extends JPanel{
    private JLabel titleLabel;
    private JPanel lPanel;
    private JCheckBox stateBox;
    private Lesson lesson;

    public LessonPanel(Lesson lesson) {
        this.lesson = lesson;
        setLayout(new BorderLayout());
        add(lPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        setBackground(Color.GRAY);
        setForeground(Color.BLACK);
        titleLabel.setText(lesson.getTitle());
        titleLabel.setBackground(Color.GRAY);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 10));
        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClickHandler(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClickHandler(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lPanel.setBackground(new Color(210, 210, 210));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lPanel.setBackground(new Color(230, 230, 230));
            }
        };

        lPanel.addMouseListener(clickListener);
        titleLabel.addMouseListener(clickListener);
        stateBox.addMouseListener(clickListener);
    }

    public void setComplete(){
        stateBox.setSelected(true);
        lPanel.revalidate();
        lPanel.repaint();
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void leftClickHandler(MouseEvent e){

    }

    public void rightClickHandler(MouseEvent e){}
}
