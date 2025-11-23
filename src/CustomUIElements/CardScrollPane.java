package CustomUIElements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public class CardScrollPane<T> extends JPanel {
    private JPanel contentPanel;
    private JTextField searchBar;
    private JButton searchButton;
    private JPanel cardPanel;
    private JPanel listPanel;

    private ArrayList<T> loadedItems;
    private ArrayList<T> availableItems;

    private final Predicate<T> filterFunction;
    private final Function<T, String> flavourFunction;
    private final CardFactory<T> cardFactory;
    private final ArrayList<T> dataSource;

    private final String noResultMessage;

    public CardScrollPane(ArrayList<T> dataSource, CardFactory<T> cardFactory, String noResultMessage,
                      Function<T, String> flavourFunction, Predicate<T> filterFunction) {
        this.dataSource = dataSource;
        this.cardFactory = cardFactory;
        this.noResultMessage = noResultMessage == null? "No items found": noResultMessage;
        this.filterFunction = filterFunction;
        this.flavourFunction = flavourFunction;
        this.availableItems = new ArrayList<>();

        setBackground(Color.GRAY);

        for (T item : dataSource) {
            if (filterFunction == null || filterFunction.test(item)) {
                availableItems.add(item);
            }
        }

        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        loadItems();

        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.setForeground(Color.BLACK);
        searchButton.addActionListener(_ -> search());

        searchBar.addActionListener(_ -> search());

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    public void loadItems() {
        availableItems = new ArrayList<>();
        for (T item : dataSource) {
            if (filterFunction == null || filterFunction.test(item)) {
                availableItems.add(item);
            }
        }
        loadedItems = new ArrayList<>(availableItems);
        displayLoadedItems();
    }

    public void displayLoadedItems() {
        if (!loadedItems.isEmpty()) {
            cardPanel.removeAll();
            cardPanel.revalidate();
            cardPanel.repaint();

            for (T item : loadedItems) {
                String flavour = null;
                if (flavourFunction != null) {
                    flavour = flavourFunction.apply(item);
                }

                GenericCard<T> card = createCard(item, flavour);
                cardPanel.add(card);
                cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            cardPanel.removeAll();
            JPanel wrapper = new JPanel();
            wrapper.setBackground(Color.gray);
            wrapper.setBorder(new EmptyBorder(10, 10,10,10));
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
            JLabel noResults = new JLabel(noResultMessage);
            noResults.setFont(new Font(null, Font.PLAIN, 20));
            wrapper.add(noResults);
            cardPanel.add(wrapper);
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }

    private GenericCard<T> createCard(T item, String flavour) {
        GenericCard<T> card = cardFactory.createCard(item, flavour);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    card.leftClickHandler(e);
                    CardScrollPane.this.leftClickHandler(e, card);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    card.rightClickHandler(e);
                    CardScrollPane.this.rightClickHandler(e, card);
                }
            }
        });

//        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        return card;
    }

    public void search() {
        String key = searchBar.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            loadedItems = new ArrayList<>(availableItems);
        } else {
            ArrayList<T> searched = new ArrayList<>();
            for (T item : availableItems) {
                GenericCard<T> tempCard = cardFactory.createCard(item, null);
                if (tempCard.getSearchableText().toLowerCase().contains(key)) {
                    searched.add(item);
                }
            }
            loadedItems = searched;
        }
        displayLoadedItems();
    }

    public void rightClickHandler(MouseEvent e, GenericCard<T> card) {

    }

    public void leftClickHandler(MouseEvent e, GenericCard<T> card) {

    }
}
