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
    private Course course;
    private Lesson activeLesson = null;
    private JTextPane content = new JTextPane();
    private JPanel coursesPanel = new JPanel();

    public EditableCourseView(Course course, Instructor instructor, CourseDatabaseManager courseDB, UserDatabaseManager userDB) {
        this.userDB = userDB;
        this.courseDB = courseDB;
        this.course = course;

        setLayout(new BorderLayout());
        add(ecvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));

        lessonTitle.setVisible(false);

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        generateSideBar();

        saveExitButton.setBackground(Color.LIGHT_GRAY);
        saveExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course.setDescription(descriptionTextPane.getText());
                course.setTitle(courseTitle.getText());
                courseDB.saveToFile();
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

    public void generateSideBar(){
        coursesPanel.removeAll();
        coursesPanel.repaint();
        coursesPanel.revalidate();

        JButton addChapterButton = new JButton("Add Chapter");
        addChapterButton.setBackground(Color.LIGHT_GRAY);
        addChapterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addChapter(e);
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
            CollapsablePanel cur = new CollapsablePanel(chapter.getChapterID(), chapter.getTitle()){
                @Override
                public void rightClickHandler(MouseEvent e){
                    final JPopupMenu popupMenu = new JPopupMenu();

                    JMenuItem changeOrder = new JMenuItem("Change Order");
                    changeOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    changeOrder.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changeChapterOrder(e);
                        }
                    });

                    JMenuItem deleteChapter = new JMenuItem("Delete Chapter");
                    deleteChapter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    deleteChapter.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            deleteChapter(e);
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
                    addLesson(e);
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
                                changeLessonOrder(e);
                            }
                        });

                        JMenuItem deleteLesson = new JMenuItem("Delete Lesson");
                        deleteLesson.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        deleteLesson.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                deleteLesson(e);
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
    }

    private void changeChapterOrder(ActionEvent e){
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        CollapsablePanel panel =  (CollapsablePanel) popupMenu.getParent();

        Chapter chapter = course.getChapterById(panel.getId());

        if (chapter != null){
            String input = JOptionPane.showInputDialog(this, "Enter new Order for" + chapter.getTitle() + ":" + chapter.getOrder());

            if (input != null && !input.trim().isEmpty()){
                try{
                    int newOrder = Integer.parseInt(input.trim());
                    chapter.setOrder(newOrder);
                    courseDB.saveToFile();
                    generateSideBar();
                }catch(NumberFormatException nfe){
                    JOptionPane.showMessageDialog(this, "Order Must be an integer","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void changeLessonOrder(ActionEvent e){
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        LessonPanel panel =  (LessonPanel) popupMenu.getParent();
        Lesson lesson = panel.getLesson();

        if (lesson != null){
            String input = JOptionPane.showInputDialog(this, "Enter new Order for" + lesson.getTitle() + ":" + lesson.getOrder());
            if (input != null && !input.trim().isEmpty()){
                try{
                    int newOrder = Integer.parseInt(input.trim());
                    lesson.setOrder(newOrder);
                    courseDB.saveToFile();
                    generateSideBar();
                }catch(NumberFormatException nfe){
                    JOptionPane.showMessageDialog(this, "Order Must be an integer","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void addChapter(ActionEvent e){

        courseDB.saveToFile();
        generateSideBar();
    }

    private void addLesson(ActionEvent e){

        courseDB.saveToFile();
        generateSideBar();
    }

    private void deleteChapter(ActionEvent e){
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        CollapsablePanel panel =  (CollapsablePanel) popupMenu.getInvoker().getParent();

        Chapter chapter = course.getChapterById(panel.getId());

        if (chapter != null){
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Chapter " + chapter.getTitle() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION){
                if (activeLesson != null && activeLesson.getChapterID().equals(chapter.getChapterID())) {
                    activeLesson = null;
                    lessonTitle.setVisible(false);
                    changeContentPanel(new JPanel());
                }
                course.deleteChapter(chapter);
                courseDB.saveToFile();
                generateSideBar();
            }
        }
    }

    private void deleteLesson(ActionEvent e){
        Component source = (Component) e.getSource();
        JPopupMenu popup = (JPopupMenu) source.getParent();
        LessonPanel lessonPanel = (LessonPanel) popup.getInvoker();
        Lesson lesson = lessonPanel.getLesson();

        if (lesson != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete lesson '" + lesson.getTitle() + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (activeLesson != null && activeLesson.getLessonID().equals(lesson.getLessonID())) {
                    activeLesson = null;
                    lessonTitle.setVisible(false);
                    changeContentPanel(new JPanel());
                }
                Chapter parentChapter = course.getChapterById(lesson.getChapterID());
                if (parentChapter != null) {
                    parentChapter.removeLesson(lesson);
                }
                courseDB.saveToFile();
                generateSideBar();
            }
        }
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
