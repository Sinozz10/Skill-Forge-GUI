package MainUI;

import CustomDataTypes.Chapter;
import CustomDataTypes.Course;
import CustomDataTypes.Lesson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import CustomDataTypes.*;
import CustomUIElements.*;
import DataManagment.*;

public class LessonManager extends JPanel {
    private final JTextField chapterID;
    private final JTextField courseID;
    private final JTextField title;
    private final JTextArea content;
    private final JTextField orderField;
    private JButton addButton;
    private JButton deleteButton;
    private final CourseDatabaseManager courseDB;
    private final GenerationID idGenerator;

    public LessonManager(CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.courseDB = courseDB;
        this.idGenerator = new GenerationID(userDB, courseDB);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        setBackground(Color.LIGHT_GRAY);

        // Add/Delete
        JPanel addDeletePanel = new JPanel();
        addDeletePanel.setLayout(new BorderLayout());

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
        addButton.setForeground(Color.BLACK);
        formPanel.add(addButton);

        deleteButton = new JButton("Delete Lesson");
        deleteButton.setBackground(Color.LIGHT_GRAY);
        deleteButton.setForeground(Color.BLACK);
        formPanel.add(deleteButton);

        addDeletePanel.add(formPanel, BorderLayout.CENTER);

        // Edit
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());

        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(8, 2, 10, 10));

        editorPanel.add(new JLabel("Lesson ID:"));
        JTextField lessonIDField = new JTextField();
        editorPanel.add(lessonIDField);

        editorPanel.add(new JLabel("Current Chapter:"));
        JLabel currentChapterLabel = new JLabel("Not loaded");
        currentChapterLabel.setForeground(Color.GRAY);
        editorPanel.add(currentChapterLabel);

        editorPanel.add(new JLabel("Title:"));
        JTextField editTitleField = new JTextField();
        editTitleField.setEnabled(false);
        editorPanel.add(editTitleField);

        editorPanel.add(new JLabel("Order:"));
        JTextField editOrderField = new JTextField();
        editOrderField.setEnabled(false);
        editorPanel.add(editOrderField);

        editorPanel.add(new JLabel("Content:"));
        JTextArea editContentArea = new JTextArea(3, 20);
        editContentArea.setEnabled(false);
        editContentArea.setLineWrap(true);
        editContentArea.setWrapStyleWord(true);
        JScrollPane editContentScroll = new JScrollPane(editContentArea);
        editorPanel.add(editContentScroll);

        editorPanel.add(new JLabel("Move to Chapter ID:"));
        JTextField newChapterIDField = new JTextField();
        newChapterIDField.setEnabled(false);
        newChapterIDField.setToolTipText("Leave empty to keep in current chapter");
        editorPanel.add(newChapterIDField);

        JButton loadButton = new JButton("Load Lesson");
        loadButton.setBackground(Color.LIGHT_GRAY);
        loadButton.setForeground(Color.BLACK);
        editorPanel.add(loadButton);

        JButton updateButton = new JButton("Update Lesson");
        updateButton.setBackground(Color.LIGHT_GRAY);
        updateButton.setForeground(Color.BLACK);
        updateButton.setEnabled(false);
        editorPanel.add(updateButton);

        editPanel.add(editorPanel, BorderLayout.CENTER);

        // Add tabs
        tabbedPane.addTab("Add/Delete", addDeletePanel);
        tabbedPane.addTab("Edit", editPanel);
        add(tabbedPane, BorderLayout.CENTER);

        // key listeners lel enter
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

        final Lesson[] currentLesson = {null};
        final Chapter[] currentChapter = {null};
        final Course[] currentCourse = {null};

        lessonIDField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLoadLesson(lessonIDField, currentChapterLabel, editTitleField, editOrderField, editContentArea, newChapterIDField, updateButton, currentLesson, currentChapter, currentCourse);
                }
            }
        });

        editTitleField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    editOrderField.requestFocus();
                }
            }
        });

        editOrderField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    editContentArea.requestFocus();
                }
            }
        });

        editContentArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    newChapterIDField.requestFocus();
                }
            }
        });

        newChapterIDField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleUpdateLesson(lessonIDField, currentChapterLabel, editTitleField, editOrderField,
                            editContentArea, newChapterIDField, updateButton, currentLesson, currentChapter, currentCourse);
                }
            }
        });

        loadButton.addActionListener(e -> handleLoadLesson(lessonIDField, currentChapterLabel, editTitleField, editOrderField,
                editContentArea, newChapterIDField, updateButton, currentLesson, currentChapter, currentCourse));
        updateButton.addActionListener(e -> handleUpdateLesson(lessonIDField, currentChapterLabel, editTitleField, editOrderField,
                editContentArea, newChapterIDField, updateButton, currentLesson, currentChapter, currentCourse));
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
    private void handleLoadLesson(JTextField lessonIDField, JLabel currentChapterLabel, JTextField editTitleField,
                                  JTextField editOrderField, JTextArea editContentArea, JTextField newChapterIDField,
                                  JButton updateButton, Lesson[] currentLesson, Chapter[] currentChapter, Course[] currentCourse) {
        String lessonID = lessonIDField.getText().trim();

        if (lessonID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lesson ID is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Course course : courseDB.getRecords()) {
            for (Chapter chapter : course.getChapters()) {
                for (Lesson lesson : chapter.getLessons()) {
                    if (lesson.getLessonID().equals(lessonID)) {
                        currentLesson[0] = lesson;
                        currentChapter[0] = chapter;
                        currentCourse[0] = course;

                        editTitleField.setText(lesson.getTitle());
                        editOrderField.setText(String.valueOf(lesson.getOrder()));
                        editContentArea.setText(lesson.getContent());
                        currentChapterLabel.setText(chapter.getChapterID() + " - " + chapter.getTitle());
                        currentChapterLabel.setForeground(Color.BLACK);

                        editTitleField.setEnabled(true);
                        editOrderField.setEnabled(true);
                        editContentArea.setEnabled(true);
                        newChapterIDField.setEnabled(true);
                        updateButton.setEnabled(true);

                        JOptionPane.showMessageDialog(this, "Lesson loaded successfully!\nCustomDataTypes.Course: " + course.getTitle() +
                                "\nCustomDataTypes.Chapter: " + chapter.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Lesson not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void handleUpdateLesson(JTextField lessonIDField, JLabel currentChapterLabel, JTextField editTitleField, JTextField editOrderField,
                                    JTextArea editContentArea, JTextField newChapterIDField, JButton updateButton,
                                    Lesson[] currentLesson, Chapter[] currentChapter, Course[] currentCourse) {
        if (currentLesson[0] == null || currentChapter[0] == null || currentCourse[0] == null) {
            JOptionPane.showMessageDialog(this, "Please load a lesson first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titleText = editTitleField.getText().trim();
        String orderStr = editOrderField.getText().trim();
        String contentText = editContentArea.getText().trim();
        String newChapterID = newChapterIDField.getText().trim();

        if (titleText.isEmpty() || orderStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Order are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int newOrder = Integer.parseInt(orderStr);
            Chapter targetChapter = currentChapter[0];

            if (!newChapterID.isEmpty()) {
                Chapter foundChapter = currentCourse[0].getChapterById(newChapterID);
                if (foundChapter == null) {
                    JOptionPane.showMessageDialog(this, "Target chapter not found in the same course!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                targetChapter = foundChapter;
            }

            for (Lesson lesson : targetChapter.getLessons()) {
                if (!lesson.getLessonID().equals(currentLesson[0].getLessonID()) && lesson.getOrder() == newOrder) {
                    JOptionPane.showMessageDialog(this, "A lesson with this order already exists in the target chapter!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!targetChapter.getChapterID().equals(currentChapter[0].getChapterID())) {
                currentChapter[0].removeLesson(currentLesson[0]);
                currentLesson[0].setChapterID(targetChapter.getChapterID());
                targetChapter.addLesson(currentLesson[0]);
            }

            currentLesson[0].setTitle(titleText);
            currentLesson[0].setOrder(newOrder);
            currentLesson[0].setContent(contentText);

            courseDB.updateRecord(currentCourse[0]);
            courseDB.saveToFile();

            String message = "Lesson updated successfully!";
            if (!targetChapter.getChapterID().equals(currentChapter[0].getChapterID())) {
                message += "\nMoved from: " + currentChapter[0].getTitle() + "\nTo: " + targetChapter.getTitle();
            }

            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);

            lessonIDField.setText("");
            editTitleField.setText("");
            editOrderField.setText("");
            editContentArea.setText("");
            newChapterIDField.setText("");
            currentChapterLabel.setText("Not loaded");
            currentChapterLabel.setForeground(Color.GRAY);

            editTitleField.setEnabled(false);
            editOrderField.setEnabled(false);
            editContentArea.setEnabled(false);
            newChapterIDField.setEnabled(false);
            updateButton.setEnabled(false);

            currentLesson[0] = null;
            currentChapter[0] = null;
            currentCourse[0] = null;
            lessonIDField.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
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