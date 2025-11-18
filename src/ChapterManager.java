import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChapterManager extends JPanel {
    //Attributes
    private JTextField courseID;
    private JTextField orderField;
    private JTextField chapterTitle;
    private JButton addChapterButton;
    private JButton deleteChapterButton;
    private JPanel managerPanel;
    private final CourseDatabaseManager courseDB;
    private final GenerationID idGenerator;

    //Constructor
    public ChapterManager(CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.courseDB = courseDB;
        this.idGenerator = new GenerationID(userDB,courseDB);
        setLayout(new BorderLayout());

        managerPanel = new JPanel();
        managerPanel.setLayout(new GridLayout(5, 2, 10, 10));

        managerPanel.add(new JLabel("Course ID:"));
        courseID = new JTextField();
        managerPanel.add(courseID);

        managerPanel.add(new JLabel("Order:"));
        orderField = new JTextField();
        managerPanel.add(orderField);

        managerPanel.add(new JLabel("Chapter Title:"));
        courseID = new JTextField();
        managerPanel.add(chapterTitle);

        addChapterButton = new JButton("Add Chapter");
        addChapterButton.setBackground(Color.LIGHT_GRAY);
        managerPanel.add(addChapterButton);

        deleteChapterButton = new JButton("Delete Chapter");
        deleteChapterButton.setBackground(Color.LIGHT_GRAY);
        managerPanel.add(deleteChapterButton);

        add(managerPanel, BorderLayout.CENTER);

        addChapterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddChapter();
            }
        });

        deleteChapterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteChapter();
            }
        });

        // 7atet action listeners for buttons to respond to ENTER.
        courseID.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    orderField.requestFocus();
                }
            }
        });

        orderField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    chapterTitle.requestFocus();
                }
            }
        });

        chapterTitle.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleAddChapter();
                }
            }
        });
    }

    private void handleAddChapter() {
        String cID = courseID.getText().trim();
        String orderStr = orderField.getText().trim();
        String chapterTitleStr = chapterTitle.getText().trim();

        if (cID.isEmpty() || orderStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = courseDB.getRecordByID(cID);
        if (course == null) {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int order = Integer.parseInt(orderStr);
            for (Chapter ch : course.getChapters()) {
                if (ch.getOrder() == order) {
                    JOptionPane.showMessageDialog(this, "A chapter with this order already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            String chapID = idGenerator.generateChapterID();
            Chapter newChapter = new Chapter(chapID, cID, chapterTitleStr , order);
            course.addChapter(newChapter);
            courseDB.updateRecord(course);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(this, "Chapter added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteChapter() {
        String cID = courseID.getText().trim();
        String orderStr = orderField.getText().trim();

        if (cID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course ID required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = courseDB.getRecordByID(cID);
        if (course == null) {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (course.getChapters().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No chapters to delete!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int order = Integer.parseInt(orderStr);
            Chapter chapterToDelete = null;
            for (Chapter ch : course.getChapters()) {
                if (ch.getOrder() == order) {
                    chapterToDelete = ch;
                    break;
                }
            }

            if (chapterToDelete == null) {
                JOptionPane.showMessageDialog(this, "Chapter not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete chapter " + chapterToDelete.getChapterID() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                course.deleteChapter(chapterToDelete);
                courseDB.updateRecord(course);
                courseDB.saveToFile();
                JOptionPane.showMessageDialog(this, "Chapter deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        courseID.setText("");
        orderField.setText("");
        courseID.requestFocus();
    }
}