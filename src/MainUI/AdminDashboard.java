package MainUI;

import CustomDataTypes.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends DashBoard {
    private final Admin admin;
    public AdminDashboard(Admin admin) {
        this.admin = admin;

    }

    @Override
    void handleHomeButton(){
        JLabel userLabel = new JLabel("Welcome " + admin.getUsername());
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        userLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        JPanel userPanel = new JPanel();
        userPanel.add(userLabel);
        changeContentPanel(userPanel);
    }
}
