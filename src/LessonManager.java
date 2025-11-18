import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LessonManager extends JPanel {
    private JTextField chapterID;
    private JTextField courseID;
    private JTextField title;
    private JTextArea content;
    private JTextField orderField;
    private JButton addButton;
    private JButton deleteButton;
    private final CourseDatabaseManager courseDB;
    private final GenerationID idGenerator;

    public LessonManager(CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.courseDB = courseDB;
        this.idGenerator = new GenerationID(userDB,courseDB);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2, 10, 10));

        formPanel.add(new JLabel("Course ID:"));
        courseID = new JTextField();
        formPanel.add(courseID);

        formPanel.add(new JLabel("Chapter ID:"));
        chapterID = new JTextField();
        formPanel.add(chapterID);

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
        formPanel.add(addButton);

        deleteButton = new JButton("Delete Lesson");
        deleteButton.setBackground(Color.LIGHT_GRAY);
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.CENTER);

        // 7atet action listeners for buttons to respond to ENTER.
        courseID.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    chapterID.requestFocus();
                }
            }
        });

        chapterID.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    title.requestFocus();
                }
            }
        });

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
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    handleAdd();
                }
            }
        });

        addButton.addActionListener(e -> handleAdd());
        deleteButton.addActionListener(e -> handleDelete());
    }

    private void handleAdd() {
        String cID = courseID.getText().trim();
        String chapID = chapterID.getText().trim();
        String t = title.getText().trim();
        String c = content.getText().trim();
        String orderStr = orderField.getText().trim();

        if (cID.isEmpty() || chapID.isEmpty() || t.isEmpty() || orderStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = courseDB.getRecordByID(cID);
        if (course == null) {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Chapter chapter = course.getChapterById(chapID);
        if (chapter == null) {
            JOptionPane.showMessageDialog(this, "Chapter not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int order = Integer.parseInt(orderStr);
            String lID = idGenerator.generateLessonID();
            Lesson newLesson = new Lesson(lID, chapID, t, c, order);
            chapter.addLesson(newLesson);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(this, "Lesson added!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        String cID = courseID.getText().trim();
        String chapID = chapterID.getText().trim();
        String orderStr = orderField.getText().trim();

        if (cID.isEmpty() || chapID.isEmpty() || orderStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "IDs required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = courseDB.getRecordByID(cID);
        if (course == null) {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Chapter chapter = course.getChapterById(chapID);
        if (chapter == null) {
            JOptionPane.showMessageDialog(this, "Chapter not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int order = Integer.parseInt(orderStr);
            Lesson lessonToDelete = null;
            for (Lesson l : chapter.getLessons()) {
                if (l.getOrder() == order) {
                    lessonToDelete = l;
                    break;
                }
            }

            if (lessonToDelete == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete lesson?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                chapter.removeLesson(lessonToDelete);
                courseDB.updateRecord(course);
                courseDB.saveToFile();
                JOptionPane.showMessageDialog(this, "Lesson deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }

        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        courseID.setText("");
        chapterID.setText("");
        title.setText("");
        content.setText("");
        orderField.setText("");
        courseID.requestFocus();
    }
}