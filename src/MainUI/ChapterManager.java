package MainUI;

import CustomDataTypes.*;
import CustomUIElements.*;
import DataManagment.*;

import CustomDataTypes.Chapter;
import CustomDataTypes.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChapterManager extends JPanel {
    //Attributes
    private final JTextField courseID;
    private final JTextField orderField;
    private final JTextField chapterTitle;
    private JButton addChapterButton;
    private JButton deleteChapterButton;
    private JPanel managerPanel;
    private final CourseDatabaseManager courseDB;
    private final GenerationID idGenerator;

    //Constructor
    public ChapterManager(CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.courseDB = courseDB;
        this.idGenerator = new GenerationID(userDB, courseDB);
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add/Delete
        JPanel addDeletePanel = new JPanel();
        addDeletePanel.setLayout(new BorderLayout());

        managerPanel = new JPanel();
        managerPanel.setLayout(new GridLayout(6, 2, 10, 10));

        managerPanel.add(new JLabel("CustomDataTypes.Course ID:"));
        courseID = new JTextField();
        managerPanel.add(courseID);

        managerPanel.add(new JLabel("Order:"));
        orderField = new JTextField();
        managerPanel.add(orderField);

        managerPanel.add(new JLabel("CustomDataTypes.Chapter Title:"));
        chapterTitle = new JTextField();
        managerPanel.add(chapterTitle);

        addChapterButton = new JButton("Add CustomDataTypes.Chapter");
        addChapterButton.setBackground(Color.LIGHT_GRAY);
        managerPanel.add(addChapterButton);

        deleteChapterButton = new JButton("Delete CustomDataTypes.Chapter");
        deleteChapterButton.setBackground(Color.LIGHT_GRAY);
        managerPanel.add(deleteChapterButton);

        addDeletePanel.add(managerPanel, BorderLayout.CENTER);

        //Edit
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());

        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(5, 2, 10, 10));

        editorPanel.add(new JLabel("CustomDataTypes.Chapter ID:"));
        JTextField chapterIDField = new JTextField();
        editorPanel.add(chapterIDField);

        editorPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        titleField.setEnabled(false);
        editorPanel.add(titleField);

        editorPanel.add(new JLabel("Order:"));
        JTextField editOrderField = new JTextField();
        editOrderField.setEnabled(false);
        editorPanel.add(editOrderField);

        JButton loadButton = new JButton("Load CustomDataTypes.Chapter");
        loadButton.setBackground(Color.LIGHT_GRAY);
        editorPanel.add(loadButton);

        JButton updateButton = new JButton("Update CustomDataTypes.Chapter");
        updateButton.setBackground(Color.LIGHT_GRAY);
        updateButton.setEnabled(false);
        editorPanel.add(updateButton);

        editPanel.add(editorPanel, BorderLayout.CENTER);
        tabbedPane.addTab("Add/Delete", addDeletePanel);
        tabbedPane.addTab("Edit", editPanel);
        add(tabbedPane, BorderLayout.CENTER);

        // 7atet listeners lel enter 3ashan yb2a smooth
        addChapterButton.addActionListener(e -> handleAddChapter());
        deleteChapterButton.addActionListener(e -> handleDeleteChapter());

        final Chapter[] currentChapter = {null}; //Fadyen
        final Course[] currentCourse = {null}; //Fadyen

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

        chapterIDField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLoadChapter(chapterIDField, titleField, editOrderField, updateButton, currentChapter, currentCourse);
                }
            }
        });

        titleField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    editOrderField.requestFocus();
                }
            }
        });

        editOrderField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleUpdateChapter(chapterIDField, titleField, editOrderField, updateButton, currentChapter, currentCourse);
                }
            }
        });

        loadButton.addActionListener(e -> handleLoadChapter(chapterIDField, titleField, editOrderField, updateButton, currentChapter, currentCourse));
        updateButton.addActionListener(e -> handleUpdateChapter(chapterIDField, titleField, editOrderField, updateButton, currentChapter, currentCourse));
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
            JOptionPane.showMessageDialog(this, "CustomDataTypes.Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteChapter() {
        String cID = courseID.getText().trim();
        String orderStr = orderField.getText().trim();

        if (cID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CustomDataTypes.Course ID required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = courseDB.getRecordByID(cID);
        if (course == null) {
            JOptionPane.showMessageDialog(this, "CustomDataTypes.Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete chapter " + chapterToDelete.getChapterID() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                course.deleteChapter(chapterToDelete);
                courseDB.updateRecord(course);
                courseDB.saveToFile();
                JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoadChapter(JTextField chapterIDField, JTextField titleField, JTextField editOrderField, JButton updateButton,
                                   Chapter[] currentChapter, Course[] currentCourse) {
        String chapID = chapterIDField.getText().trim();

        if (chapID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter ID is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Course course : courseDB.getRecords()) {
            for (Chapter chapter : course.getChapters()) {
                if (chapter.getChapterID().equals(chapID)) {
                    currentChapter[0] = chapter;
                    currentCourse[0] = course;

                    titleField.setText(chapter.getTitle());
                    editOrderField.setText(String.valueOf(chapter.getOrder()));
                    titleField.setEnabled(true);
                    editOrderField.setEnabled(true);
                    updateButton.setEnabled(true);

                    JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter loaded successfully!\nCustomDataTypes.Course: " + course.getTitle(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void handleUpdateChapter(JTextField chapterIDField, JTextField titleField, JTextField editOrderField, JButton updateButton,
                                     Chapter[] currentChapter, Course[] currentCourse) {
        if (currentChapter[0] == null || currentCourse[0] == null) {
            JOptionPane.showMessageDialog(this, "Please load a chapter first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String title = titleField.getText().trim();
        String orderStr = editOrderField.getText().trim();

        if (title.isEmpty() || orderStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int newOrder = Integer.parseInt(orderStr);

            for (Chapter ch : currentCourse[0].getChapters()) {
                if (!ch.getChapterID().equals(currentChapter[0].getChapterID()) && ch.getOrder() == newOrder) {
                    JOptionPane.showMessageDialog(this, "A chapter with this order already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            currentChapter[0].setTitle(title);
            currentChapter[0].setOrder(newOrder);

            courseDB.updateRecord(currentCourse[0]);
            courseDB.saveToFile();

            JOptionPane.showMessageDialog(this, "CustomDataTypes.Chapter updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            chapterIDField.setText("");
            titleField.setText("");
            editOrderField.setText("");
            titleField.setEnabled(false);
            editOrderField.setEnabled(false);
            updateButton.setEnabled(false);
            currentChapter[0] = null;
            currentCourse[0] = null;
            chapterIDField.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        courseID.setText("");
        orderField.setText("");
        chapterTitle.setText("");
        courseID.requestFocus();
    }
}