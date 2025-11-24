package Dialogs;

import CustomDataTypes.TextQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextQuestionPanel extends JPanel {
    private JTextField title;
    private JTextField correctAns;
    private JTextField order;
    private JButton addButton;
    private JButton cancelButton;
    private final QuestionDialog parent;
    private final JPanel contentPanel = new JPanel();

    public TextQuestionPanel(QuestionDialog parent) {
        this.parent = parent;

        initComponents();
        addListeners();
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    public void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Question:"));
        title = new JTextField();
        formPanel.add(title);

        formPanel.add(new JLabel("Answer:"));
        correctAns = new JTextField();
        formPanel.add(correctAns);

        formPanel.add(new JLabel("Order:"));
        order = new JTextField();
        formPanel.add(order);

        addButton = new JButton("Add Question");
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.setForeground(Color.BLACK);
        formPanel.add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        formPanel.add(cancelButton);

        contentPanel.add(formPanel, BorderLayout.CENTER);
    }

    public void addListeners() {
        title.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    correctAns.requestFocus();
                }
            }
        });

        correctAns.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    order.requestFocus();
                }
            }
        });

        order.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createQuestion();
                }
            }
        });


        addButton.addActionListener(_ -> createQuestion());
        cancelButton.addActionListener(_ -> handleCancel());
    }

    private void createQuestion() {
        String t = title.getText().trim();
        String ans = correctAns.getText().trim();
        String orderStr = order.getText().trim();

        try {
            parent.setQuestion(new TextQuestion(t, ans, Integer.parseInt(orderStr)));
            parent.dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order must be number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        parent.dispose();
    }
}
