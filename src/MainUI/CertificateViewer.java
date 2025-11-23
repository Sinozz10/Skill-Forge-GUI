package MainUI;

import CustomDataTypes.Certificate;
import CustomDataTypes.Student;
import DataManagment.CertificateGenerator;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CertificateViewer extends JPanel {
    private Student student;

    public CertificateViewer(Student student) {
        this.student = student;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // Title
        JLabel title = new JLabel("My Certificates", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Check if empty
        if (student.getCertificates().isEmpty()) {
            add(new JLabel("No certificates yet. Complete a course!", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        // Show certificates
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.LIGHT_GRAY);

        for (Certificate cert : student.getCertificates()) {
            list.add(makeCard(cert));
            list.add(Box.createVerticalStrut(10));
        }

        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    private JPanel makeCard(Certificate cert) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setMaximumSize(new Dimension(9999, 100));

        // Info
        JLabel info = new JLabel("<html><b>" + cert.getCourseName() + "</b><br>" +
                "ID: " + cert.getCertificateID() + "<br>" +
                "Date: " + cert.getFormattedDate() + "</html>");
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        JButton viewBtn = new JButton("View");
        viewBtn.setBackground(new Color(39, 85, 117));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.addActionListener(e -> viewCertificate(cert));

        JButton downloadBtn = new JButton("Download");
        downloadBtn.setBackground(new Color(76, 175, 80));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.addActionListener(e -> downloadCertificate(cert));

        buttons.add(viewBtn);
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(downloadBtn);

        card.add(info, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.EAST);
        return card;
    }

    private void viewCertificate(Certificate cert) {
        try {
            // Generate if doesn't exist
            if (!CertificateGenerator.exists(cert.getCertificateID())) {
                new CertificateGenerator().generateCertificate(cert);
            }

            // Open
            Desktop.getDesktop().open(new File(CertificateGenerator.getPath(cert.getCertificateID())));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void downloadCertificate(Certificate cert) {
        try {
            // Generate if doesn't exist
            if (!CertificateGenerator.exists(cert.getCertificateID())) {
                new CertificateGenerator().generateCertificate(cert);
            }

            // Choose location
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(cert.getCertificateID() + ".pdf"));

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File destination = chooser.getSelectedFile();
                File source = new File(CertificateGenerator.getPath(cert.getCertificateID()));

                // Copy file
                java.nio.file.Files.copy(
                        source.toPath(),
                        destination.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                JOptionPane.showMessageDialog(this, "Certificate saved to:\n" + destination.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}