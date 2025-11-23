package CustomUIElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CollapsablePanel extends JPanel {
    private JPanel cPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel arrowLabel;
    private JPanel contentPanel;
    private String title;
    private final String id;
    private boolean expanded = true;

    public CollapsablePanel(String id, String title) {
        this.title = title;
        this.id = id;
        setLayout(new BorderLayout());
        add(cPanel, BorderLayout.CENTER);
        setForeground(Color.BLACK);
        setBackground(Color.GRAY);

        headerPanel.setBackground(Color.lightGray);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        headerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleLabel.setText(title);
        titleLabel.setBackground(Color.GRAY);
        titleLabel.setForeground(Color.BLACK);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClickHandler();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClickHandler(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                headerPanel.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                headerPanel.setBackground(Color.lightGray);
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
        arrowLabel.setForeground(Color.BLACK);

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

    public void clearContent() {
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public boolean getExpanded() {
        return expanded;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void leftClickHandler() {
        toggleExpanded();
    }

    public void rightClickHandler(MouseEvent e) {
    }
}
