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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Collapsible Panel Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 500);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            Chapter test1 = new Chapter("cid", "Cid", "Section 1", 2);
            CollapsablePanel panel1 = new CollapsablePanel(test1.getTitle());
            for (int i = 1; i <= 6; i++) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new LessonPanel("Lesson" + i));
                panel1.addContent(row);

            }

            Chapter test2 = new Chapter("cid", "Cid", "Section 2", 2);
            CollapsablePanel panel2 = new CollapsablePanel(test2.getTitle());
            for (int i = 1; i <= 6; i++) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new LessonPanel("Lesson" + i));
                panel2.addContent(row);
            }

            Chapter test3 = new Chapter("cid", "Cid", "Section 3", 2);
            CollapsablePanel panel3 = new CollapsablePanel(test3.getTitle());
            for (int i = 1; i <= 6; i++) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new LessonPanel("Lesson" + i));
                panel3.addContent(row);
            }

            mainPanel.add(panel1);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            mainPanel.add(panel2);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            mainPanel.add(panel3);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Wrap in scroll pane
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            frame.add(scrollPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });}
}
