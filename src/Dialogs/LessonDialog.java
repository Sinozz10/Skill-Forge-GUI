package Dialogs;

import CustomDataTypes.Chapter;
import CustomDataTypes.Lesson;
import DataManagment.CourseDatabaseManager;
import DataManagment.GenerationID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LessonDialog extends JDialog {
    private JTextField title;
    private JTextArea content;
    private JTextField orderField;
    private JButton addButton;
    private JButton cancelButton;
    private Lesson lesson = null;
    private Chapter chapter;
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();

    public LessonDialog(Frame parent, Chapter chapter) {
        super(parent, "Add new Lesson", true);
        this.chapter = chapter;

        initComponents();
        addListeners();

        pack();
        setLocationRelativeTo(this);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents(){
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(4, 2, 10, 10));

            formPanel.add(new JLabel("Title:"));
            title = new JTextField();
            formPanel.add(title);

            formPanel.add(new JLabel("Order:"));
            orderField = new JTextField();
            formPanel.add(orderField);

            formPanel.add(new JLabel("Content:"));
            content = new JTextArea(3, 20);
            JScrollPane scroll = new JScrollPane(content);
            formPanel.add(scroll);

            addButton = new JButton("Add Lesson");
            addButton.setBackground(Color.LIGHT_GRAY);
            addButton.setForeground(Color.BLACK);
            formPanel.add(addButton);

            cancelButton = new JButton("Cancel");
            cancelButton.setBackground(Color.LIGHT_GRAY);
            cancelButton.setForeground(Color.BLACK);
            formPanel.add(cancelButton);

            add(formPanel, BorderLayout.CENTER);
    }

    private void addListeners(){
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
                    content.requestFocus();
                }
            }
        });

        content.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createLesson();
                }
            }
        });

        addButton.addActionListener(e -> createLesson());
        cancelButton.addActionListener(e -> handleCancel());
    }

    private void createLesson(){
        GenerationID generator = new GenerationID();

        String chapID = chapter.getChapterID();
        String lessID = generator.generateLessonID();
        String t = title.getText().trim();
        String c = content.getText().trim();
        String orderStr = orderField.getText().trim();

        try {
            lesson = new Lesson(lessID, chapID, t, c, Integer.parseInt(orderStr));
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel(){
        dispose();
    }

    public Lesson getResult(){
        return lesson;
    }
}
