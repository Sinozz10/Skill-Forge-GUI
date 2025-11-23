package CustomUIElements;

import CustomDataTypes.Certificate;
import DataManagment.CertificateGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CertificateCard extends GenericCard<Certificate> {
    private JPanel root;
    private JButton viewButton;
    private JButton downloadButton;
    private JLabel titleLabel;
    private JPanel headerPanel;
    private JPanel bodyPanel;
    private JPanel buttonsPanel;
    private JTextPane bodyTextPane;

    public CertificateCard(Certificate certificate, String flavour) {
        super(certificate);
        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleLabel.setText(flavour != null ? certificate.getCourseName() + "  --  " + flavour : certificate.getCourseName());
        bodyTextPane.setEditable(false);
        bodyTextPane.setText( "ID: " + certificate.getCertificateID()+ "\nDate: " + certificate.getFormattedDate());
        bodyTextPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 5));

        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 5));

        viewButton.setBackground(new Color(39, 85, 117));
        viewButton.setForeground(Color.WHITE);
        viewButton.addActionListener(_ -> viewCertificate(certificate));

        downloadButton.setBackground(new Color(76, 175, 80));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.addActionListener(_ -> downloadCertificate(certificate));
    }

    private void viewCertificate(Certificate cert) {
        try {
            // Generate if certificate doesn't exist
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
            // Generate if certificate doesn't exist
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

    @Override
    public String getSearchableText() {
        return data.getCourseName();
    }
}
