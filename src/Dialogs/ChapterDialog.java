package Dialogs;

import CustomDataTypes.Chapter;
import CustomDataTypes.Course;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChapterDialog extends JDialog {
    private JTextField orderField;
    private JTextField title;
    private JPanel managerPanel;
    private JButton addButton;
    private JButton cancelButton;
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private Chapter chapter = null;
    private final Course course;

    public ChapterDialog(Frame parent, Course course) {
        super(parent, "Add new chapter", true);
        this.course = course;

        initComponents();
        addListeners();

        pack();
        setLocationRelativeTo(this);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Title:"));
        title = new JTextField();
        formPanel.add(title);

        formPanel.add(new JLabel("Order:"));
        orderField = new JTextField();
        formPanel.add(orderField);

        addButton = new JButton("Add Chapter");
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.setForeground(Color.BLACK);
        formPanel.add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        formPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void addListeners() {
        title.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    orderField.requestFocus();
                }
            }
        });

        orderField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createLesson();
                }
            }
        });

        addButton.addActionListener(_ -> createLesson());
        cancelButton.addActionListener(_ -> handleCancel());
    }

    private void createLesson() {
        GenerationID generator = new GenerationID();

        String courID = course.getID();
        String chapID = generator.generateChapterID();
        String t = title.getText().trim();
        String orderStr = orderField.getText().trim();

        try {
            chapter = new Chapter(chapID, courID, t, Integer.parseInt(orderStr));
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        dispose();
    }

    public Chapter getResult() {
        return chapter;
    }
}
