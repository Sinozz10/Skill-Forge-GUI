package CustomUIElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CollapsablePanel extends JPanel{
    private JPanel cPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel arrowLabel;
    private JPanel contentPanel;
    private String title;
    private boolean expanded = true;

    public CollapsablePanel(String title) {
        this.title = title;
        setLayout(new BorderLayout());
        add(cPanel, BorderLayout.CENTER);

        headerPanel.setBackground(new Color(230, 230, 230));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        headerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleLabel.setText(title);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
                headerPanel.setBackground(new Color(210, 210, 210));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                headerPanel.setBackground(new Color(230, 230, 230));
            }
        };

        headerPanel.addMouseListener(clickListener);
        titleLabel.addMouseListener(clickListener);
        arrowLabel.addMouseListener(clickListener);

        toggleExpanded();
    }

    public void toggleExpanded() {
        expanded = !expanded;
        contentPanel.setVisible(expanded);
        arrowLabel.setText(expanded ? "v" : ">");

        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        revalidate();
        repaint();

        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }

    public void setExpanded(boolean expanded) {
        if (this.expanded != expanded) {
            toggleExpanded();
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void addContent(Component comp) {
        contentPanel.add(comp);
    }

    public void clearContent(){
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void leftClickHandler(MouseEvent e){
        toggleExpanded();
    }

    public void rightClickHandler(MouseEvent e){}
}
