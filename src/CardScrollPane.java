import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CardScrollPane extends JPanel {
    private final CourseDatabaseManager courseDB;
    private JPanel contentPanel;
    private final JScrollPane scrollPane;
    private JTextField searchBar;
    private JButton searchButton;
    private JPanel cardPanel;
    private JPanel listPanel;
    private ArrayList<Course> loadedCourses;
    private ArrayList<Course> availableCourses;
    private CardScrollPaneFilter function;

    public CardScrollPane(CourseDatabaseManager courseDB, CardScrollPaneFilter function) {
        this.courseDB = courseDB;
        this.function = function;
        availableCourses = new ArrayList<>();
        for(Course course: courseDB.getRecords()){
            if (function.filter(course)){
                availableCourses.add(course);
            }
        }

        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.LIGHT_GRAY);

        scrollPane = new JScrollPane(cardPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        loadCoursesFromDatabase();

        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        searchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private ArrayList<Course> sortList(List<Course> courses){
        ArrayList<Course> sortedCourses = new ArrayList<>(courses);

        sortedCourses.sort((s1, s2) -> {
            int n1 = Integer.parseInt(s1.getID().replaceAll("[^0-9]", ""));
            int n2 = Integer.parseInt(s2.getID().replaceAll("[^0-9]", ""));
            return Integer.compare(n1, n2);
        });

        return sortedCourses;
    }

    public void loadCoursesFromDatabase(){
        availableCourses = new ArrayList<>();
        for(Course course: courseDB.getRecords()){
            if (function.filter(course)){
                availableCourses.add(course);
            }
        }
        loadedCourses = availableCourses;
        displayLoadedCourses();
    }

    public void displayLoadedCourses(){
        if (!loadedCourses.isEmpty()){
            loadedCourses = sortList(loadedCourses);

            cardPanel.removeAll();
            cardPanel.revalidate();
            cardPanel.repaint();

            for (Course course : loadedCourses) {
                Card card = new Card(course) {
                    @Override
                    public void rightClickHandler(MouseEvent e){
                        CardScrollPane.this.rightClickHandler(e);
                    }
                    @Override
                    public void leftClickHandler(MouseEvent e){
                        CardScrollPane.this.leftClickHandler(e);
                    }
                };
                card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
                cardPanel.add(card);
                cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
    }

    public void search(){
        String key = searchBar.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            loadedCourses = availableCourses;
        }else {
            ArrayList<Course> searched = new ArrayList<>();
            for (Course course : availableCourses) {
                if (course.getTitle().contains(key)){
                    searched.add(course);
                }
            }
            loadedCourses = searched;
        }
        displayLoadedCourses();
    }

    public void rightClickHandler(MouseEvent e){

    }

    public void leftClickHandler(MouseEvent e) {
    }
}
