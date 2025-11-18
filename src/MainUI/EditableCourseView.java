package MainUI;

import CustomDataTypes.*;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class EditableCourseView extends JPanel{
    private JTextField courseTitle;
    private JPanel lessonView;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JTabbedPane extrasPane;
    private JPanel descriptionPanel;
    private JPanel resourcesPanel;
    private JTextField lessonTitle;
    private JPanel ecvPanel;
    private JPanel listPanel;
    private JTextPane descriptionTextPane;
    private JScrollPane resourcesScrollPane;
    private JButton saveExitButton;
    private JPanel editPanel;
    private CourseDatabaseManager courseDB;
    private UserDatabaseManager userDB;
    private Lesson activeLesson = null;
    private JTextPane content = new JTextPane();

    public EditableCourseView(Course course, Instructor instructor, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.userDB = userDB;
        this.courseDB = courseDB;

        setLayout(new BorderLayout());
        add(ecvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));

        lessonTitle.setVisible(false);

        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

        JButton addChapterButton = new JButton("Add Chapter");
        addChapterButton.setBackground(Color.LIGHT_GRAY);
        addChapterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new BoxLayout(buttonPanel1, BoxLayout.X_AXIS));
        buttonPanel1.add(addChapterButton);
        coursesPanel.add(buttonPanel1);
        coursesPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        ArrayList<Chapter> sortedChapters = course.getChapters();
        sortedChapters.sort(Comparator.comparingInt(Chapter::getOrder));
        for (Chapter chapter: sortedChapters){
            CollapsablePanel cur = new CollapsablePanel(chapter.getTitle()){
                @Override
                public void rightClickHandler(MouseEvent e){
                    final JPopupMenu popupMenu = new JPopupMenu();

                    JMenuItem changeOrder = new JMenuItem("Change Order");
                    changeOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    changeOrder.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });

                    JMenuItem deleteChapter = new JMenuItem("Delete Chapter");
                    deleteChapter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    deleteChapter.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });

                    popupMenu.add(changeOrder);
                    popupMenu.add(deleteChapter);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            };

            JButton addLessonButton = new JButton("Add Lesson");
            addLessonButton.setBackground(Color.LIGHT_GRAY);
            addLessonButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            JPanel buttonPanel2 = new JPanel();
            buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.X_AXIS));
            buttonPanel2.add(addLessonButton);
            cur.addContent(buttonPanel2);
            cur.addContent(Box.createRigidArea(new Dimension(0, 5)));

            ArrayList<Lesson> sortedLessons = chapter.getLessons();
            sortedLessons.sort(Comparator.comparingInt(Lesson::getOrder));
            for (Lesson lesson: sortedLessons){
                LessonPanel lp = new LessonPanel(lesson){
                    @Override
                    public void leftClickHandler(MouseEvent e){
                        EditableCourseView.this.leftClickHandler(e, this, lesson);
                    }

                    @Override
                    public void rightClickHandler(MouseEvent e){
                        final JPopupMenu popupMenu = new JPopupMenu();

                        JMenuItem changeOrder = new JMenuItem("Change Order");
                        changeOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        changeOrder.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                            }
                        });

                        JMenuItem deleteLesson = new JMenuItem("Delete Lesson");
                        deleteLesson.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        deleteLesson.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                            }
                        });

                        popupMenu.add(changeOrder);
                        popupMenu.add(deleteLesson);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                };
                cur.addContent(lp);
                cur.addContent(Box.createRigidArea(new Dimension(0, 5)));
            }
            coursesPanel.add(cur);
        }

        saveExitButton.setBackground(Color.LIGHT_GRAY);
        saveExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course.setDescription(descriptionTextPane.getText());
                course.setTitle(courseTitle.getText());
            }
        });

        scrollPane = new JScrollPane(coursesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        courseTitle.setText(course.getTitle());
        descriptionTextPane.setText(course.getDescription());
    }

    public void leftClickHandler(MouseEvent e,LessonPanel Lp, Lesson lesson){
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());

        saveActiveLesson();
        activeLesson = lesson;

        content.setText(lesson.getContent());

        tempPanel.add(content, BorderLayout.CENTER);

        lessonTitle.setVisible(true);
        lessonTitle.setText(lesson.getTitle());
        changeContentPanel(tempPanel);
    }

    public void saveActiveLesson(){
        if (activeLesson != null){
            activeLesson.setTitle(lessonTitle.getText().trim());
            activeLesson.setContent(content.getText());
        }
    }

    public void changeContentPanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    static void main() {
        UserDatabaseManager userDB = new UserDatabaseManager("users.json");
        CourseDatabaseManager courseDB = new CourseDatabaseManager("courses.json");
        Course course = courseDB.getRecordByID("C0006");
        Instructor instructor = (Instructor) userDB.getRecordByID("I0002");

        JFrame frame = new JFrame();
        frame.setSize(900, 550);
        frame.setLayout(new BorderLayout());
        frame.add(new EditableCourseView(course, instructor, courseDB, userDB), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
