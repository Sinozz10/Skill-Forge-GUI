package MainUI;

import CustomDataTypes.*;
import CustomUIElements.CollapsablePanel;
import CustomUIElements.LessonPanel;
import DataManagment.CourseDatabaseManager;
import DataManagment.UserDatabaseManager;
import Dialogs.ChapterDialog;
import Dialogs.LessonDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class EditableCourseView extends JPanel {
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
    private JPanel mainPanel;
    private JScrollPane mainScrollPane;
    private JButton refreshButton;
    private JButton quizButton;
    private JPanel editPanel;
    private final CourseDatabaseManager courseDB = CourseDatabaseManager.getDatabaseInstance();
    private final UserDatabaseManager userDB = UserDatabaseManager.getDatabaseInstance();
    private final Course course;
    private Lesson activeLesson = null;
    private final JTextPane content = new JTextPane();
    private final JPanel coursesPanel = new JPanel();
    private final InstructorDashboard dashboard;
    private final ArrayList<GeneralTracker> trackers = new ArrayList<>();
    private boolean quizViewState;

    public EditableCourseView(Course course, Instructor instructor, InstructorDashboard dashboard) {
        this.course = course;
        this.dashboard = dashboard;

        setLayout(new BorderLayout());
        add(ecvPanel, BorderLayout.CENTER);
        listPanel.setMinimumSize(new Dimension(200, listPanel.getPreferredSize().height));
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(32);

        content.setPreferredSize(new Dimension(350, content.getPreferredSize().height));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        lessonTitle.setVisible(false);
        quizButton.setVisible(false);

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        generateSideBar();

        refreshButton.setBackground(Color.LIGHT_GRAY);
        refreshButton.setForeground(Color.black);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSideBar();
            }
        });

        saveExitButton.setBackground(Color.LIGHT_GRAY);
        saveExitButton.setForeground(Color.BLACK);
        saveExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course.setDescription(descriptionTextPane.getText());
                course.setTitle(courseTitle.getText());
                courseDB.saveToFile();
                dashboard.handleViewCourses();
            }
        });

        scrollPane = new JScrollPane(coursesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);

        listPanel.setLayout(new BorderLayout());
        listPanel.setBackground(Color.LIGHT_GRAY);
        listPanel.setForeground(Color.BLACK);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        courseTitle.setText(course.getTitle());
        descriptionTextPane.setText(course.getDescription());
    }

    public void generateSideBar() {
        for (Component comp: coursesPanel.getComponents()){
            if (comp instanceof CollapsablePanel panel){
                trackers.add(new GeneralTracker(panel.getId(), panel.getExpanded()));
            }
        }

        coursesPanel.removeAll();
        coursesPanel.repaint();
        coursesPanel.revalidate();

        JButton addChapterButton = new JButton("Add Chapter");
        addChapterButton.setBackground(Color.LIGHT_GRAY);
        addChapterButton.setForeground(Color.BLACK);
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
        generateChapters();
    }

    public void generateChapters(){
        ArrayList<Chapter> sortedChapters = course.getChapters();
        sortedChapters.sort(Comparator.comparingInt(Chapter::getOrder));
        for (Chapter chapter : sortedChapters) {
            CollapsablePanel cur = new CollapsablePanel(chapter.getChapterID(), chapter.getTitle()) {
                @Override
                public void rightClickHandler(MouseEvent e) {
                    final JPopupMenu popupMenu = new JPopupMenu();

                    JMenuItem changeOrder = new JMenuItem("Change Order");
                    changeOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    changeOrder.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changeChapterOrder(e);
                        }
                    });

                    JMenuItem changeTitle = new JMenuItem("Change Title");
                    changeTitle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    changeTitle.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changeChapterTitle(e);
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
                    popupMenu.add(changeTitle);
                    popupMenu.add(deleteChapter);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            };

            JButton addLessonButton = new JButton("Add Lesson");
            addLessonButton.setBackground(Color.LIGHT_GRAY);
            addLessonButton.setForeground(Color.BLACK);
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

            generateLessons(cur, chapter);
            coursesPanel.add(cur);

            Optional<GeneralTracker> result = trackers.stream().filter(tracker -> cur.getId().equals(tracker.getID())).findFirst();
            if (result.isPresent()){
                if (result.get().isTrue()){
                    cur.toggleExpanded();
                }
                trackers.remove(result.get());
            }
        }
    }

    public void generateLessons(CollapsablePanel cur, Chapter chapter){
        ArrayList<Lesson> sortedLessons = chapter.getLessons();
        sortedLessons.sort(Comparator.comparingInt(Lesson::getOrder));

        for (Lesson lesson : sortedLessons) {
            LessonPanel lp = new LessonPanel(lesson) {
                @Override
                public void leftClickHandler(MouseEvent e) {
                    EditableCourseView.this.leftClickHandler(e, this, lesson);
                }

                @Override
                public void rightClickHandler(MouseEvent e) {
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
    }

    private void changeChapterTitle(ActionEvent e) {
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        Component invoker = popupMenu.getInvoker();

        Container parent = invoker.getParent();
        while (parent != null && !(parent instanceof CollapsablePanel)) {
            parent = parent.getParent();
        }

        if (parent instanceof CollapsablePanel) {
            CollapsablePanel panel = (CollapsablePanel) parent;
            Chapter chapter = course.getChapterById(panel.getId());

            if (chapter != null) {
                String input = (String) JOptionPane.showInputDialog(
                        this, "Enter new Title for:", "Change Title",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        chapter.getTitle()
                );

                if (input != null && !input.trim().isEmpty()) {
                    try {
                        String title = input.trim();
                        chapter.setTitle(title);
                        courseDB.saveToFile();
                        generateSideBar();
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Order must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void changeChapterOrder(ActionEvent e) {
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        Component invoker = popupMenu.getInvoker();

        Container parent = invoker.getParent();
        while (parent != null && !(parent instanceof CollapsablePanel)) {
            parent = parent.getParent();
        }

        if (parent instanceof CollapsablePanel) {
            CollapsablePanel panel = (CollapsablePanel) parent;
            Chapter chapter = course.getChapterById(panel.getId());

            if (chapter != null) {
                String input = (String) JOptionPane.showInputDialog(
                        this, "Enter new order for '" + chapter.getTitle() + "':", "Change Order",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        chapter.getOrder()
                );

                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int newOrder = Integer.parseInt(input.trim());
                        int oldOrder = chapter.getOrder();
                        if (newOrder != oldOrder) {
                            if (newOrder < oldOrder) {
                                for (Chapter ch : course.getChapters()) {
                                    if (!ch.getChapterID().equals(chapter.getChapterID())) {
                                        if (ch.getOrder() >= newOrder && ch.getOrder() < oldOrder) {
                                            ch.setOrder(ch.getOrder() + 1);
                                        }
                                    }
                                }
                            } else {
                                for (Chapter ch : course.getChapters()) {
                                    if (!ch.getChapterID().equals(chapter.getChapterID())) {
                                        if (ch.getOrder() > oldOrder && ch.getOrder() <= newOrder) {
                                            ch.setOrder(ch.getOrder() - 1);
                                        }
                                    }
                                }
                            }
                            chapter.setOrder(newOrder);
                            courseDB.saveToFile();
                            generateSideBar();
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Order must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void changeLessonOrder(ActionEvent e) {
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        Component invoker = popupMenu.getInvoker();

        Container parent = invoker instanceof LessonPanel ? (LessonPanel) invoker : invoker.getParent();
        while (parent != null && !(parent instanceof LessonPanel)) {
            parent = parent.getParent();
        }

        if (parent instanceof LessonPanel) {
            LessonPanel lessonPanel = (LessonPanel) parent;
            Lesson lesson = lessonPanel.getLesson();

            if (lesson != null) {
                String input = (String) JOptionPane.showInputDialog(this, "Enter new order for '" + lesson.getTitle() + "':", "Change Order",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        lesson.getOrder()
                );

                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int newOrder = Integer.parseInt(input.trim());
                        int oldOrder = lesson.getOrder();

                        Chapter parentChapter = course.getChapterById(lesson.getChapterID());
                        if (parentChapter != null && newOrder != oldOrder) {
                            // Shift all lessons between old and new order
                            if (newOrder < oldOrder) {
                                // shift down kol lessons from newOrder to oldOrder-1
                                for (Lesson l : parentChapter.getLessons()) {
                                    if (!l.getLessonID().equals(lesson.getLessonID())) {
                                        if (l.getOrder() >= newOrder && l.getOrder() < oldOrder) {
                                            l.setOrder(l.getOrder() + 1);
                                        }
                                    }
                                }
                            } else {
                                // shift up
                                for (Lesson l : parentChapter.getLessons()) {
                                    if (!l.getLessonID().equals(lesson.getLessonID())) {
                                        if (l.getOrder() > oldOrder && l.getOrder() <= newOrder) {
                                            l.setOrder(l.getOrder() - 1);
                                        }
                                    }
                                }
                            }
                            lesson.setOrder(newOrder);
                            courseDB.saveToFile();
                            generateSideBar();
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Order must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void addChapter(ActionEvent e){
        Chapter chapter = new ChapterDialog(dashboard, course).getResult();

        if (chapter != null){
            course.addChapter(chapter);
            courseDB.saveToFile();
            generateSideBar();
        }
    }

    private void addLesson(ActionEvent e){
        Component comp =(Component) e.getSource();
        while (!(comp instanceof CollapsablePanel) && comp != null){
            comp = comp.getParent();
        }
        CollapsablePanel clickedPanel = (CollapsablePanel) comp;
        assert clickedPanel != null;
        Chapter chapter = course.getChapterById(clickedPanel.getId());

        Lesson lesson = new LessonDialog(dashboard, chapter).getResult();

        if (lesson != null){
            chapter.addLesson(lesson);
            courseDB.saveToFile();
            generateSideBar();
        }
    }

    private void deleteChapter(ActionEvent e) {
        Component source = (Component) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) source.getParent();
        Component invoker = popupMenu.getInvoker();

        Container parent = invoker.getParent();
        while (parent != null && !(parent instanceof CollapsablePanel)) {
            parent = parent.getParent();
        }

        if (parent instanceof CollapsablePanel) {
            CollapsablePanel panel = (CollapsablePanel) parent;
            Chapter chapter = course.getChapterById(panel.getId());

            if (chapter != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete Chapter '" + chapter.getTitle() + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
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
    }

    private void deleteLesson(ActionEvent e) {
        Component source = (Component) e.getSource();
        JPopupMenu popup = (JPopupMenu) source.getParent();
        Component invoker = popup.getInvoker();

        Container parent = invoker instanceof LessonPanel ? (LessonPanel) invoker : invoker.getParent();
        while (parent != null && !(parent instanceof LessonPanel)) {
            parent = parent.getParent();
        }

        if (parent instanceof LessonPanel) {
            LessonPanel lessonPanel = (LessonPanel) parent;
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
    }

    public void leftClickHandler(MouseEvent e,LessonPanel Lp, Lesson lesson){
        saveActiveLesson();
        activeLesson = lesson;

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        content.setText(activeLesson.getContent());
        tempPanel.add(content, BorderLayout.CENTER);

        quizViewState = false;
        quizButton.setVisible(true);
        quizButton.setText(lesson.hasQuiz()?"Edit Quiz":"Add Quiz");

        for (ActionListener al : quizButton.getActionListeners()) {
            quizButton.removeActionListener(al);
        }

        quizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quizViewState){
                    content.setText(activeLesson.getContent());
                    tempPanel.add(content, BorderLayout.CENTER);
                    quizButton.setText(lesson.hasQuiz()?"Edit Quiz":"Add Quiz");
                    quizViewState = false;
                    changeContentPanel(tempPanel);
                }else {
                    quizButton.setText("Back");
                    quizViewState = true;
                    changeContentPanel(new EditableQuizPanel(dashboard, lesson));
                }
            }
        });

        lessonTitle.setVisible(true);
        lessonTitle.setText(lesson.getTitle());
        changeContentPanel(tempPanel);
        generateSideBar();
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
}
