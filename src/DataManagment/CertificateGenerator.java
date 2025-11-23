package DataManagment;

import CustomDataTypes.Certificate;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CertificateGenerator {
    private static final String FOLDER = "files/certificates/";

    //
    public CertificateGenerator() {
        new File(FOLDER).mkdirs();
    }

    public String generateCertificate(Certificate certificate) throws DocumentException, FileNotFoundException {
        String filename = FOLDER + certificate.getCertificateID() + ".pdf";

        // Create PDF
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(filename));
        doc.open();

        // Blue color
        BaseColor blue = new BaseColor(39, 85, 117);

        // Add all content with spacing
        doc.add(new Paragraph("\n\n\n\n"));

        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 40, Font.BOLD, blue);
        Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        doc.add(new Paragraph("\n\n"));

        Font normalFont = new Font(Font.FontFamily.HELVETICA, 16);
        Paragraph certifies = new Paragraph("This is to certify that", normalFont);
        certifies.setAlignment(Element.ALIGN_CENTER);
        doc.add(certifies);

        doc.add(new Paragraph("\n"));

        // Student name - big and bold
        Font nameFont = new Font(Font.FontFamily.HELVETICA, 32, Font.BOLD, blue);
        Paragraph name = new Paragraph(certificate.getStudentName(), nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        doc.add(name);

        doc.add(new Paragraph("\n"));

        // "has completed"
        Paragraph completed = new Paragraph("has successfully completed the course", normalFont);
        completed.setAlignment(Element.ALIGN_CENTER);
        doc.add(completed);

        doc.add(new Paragraph("\n"));

        // Course name - medium bold
        Font courseFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, blue);
        Paragraph course = new Paragraph(certificate.getCourseName(), courseFont);
        course.setAlignment(Element.ALIGN_CENTER);
        doc.add(course);

        doc.add(new Paragraph("\n\n"));

        // Certificate ID and Date
        Font smallFont = new Font(Font.FontFamily.HELVETICA, 11);
        Paragraph details = new Paragraph(
                "Certificate ID: " + certificate.getCertificateID() +
                        "     Date: " + certificate.getFormattedDate(),
                smallFont
        );
        details.setAlignment(Element.ALIGN_CENTER);
        doc.add(details);

        doc.add(new Paragraph("\n\n"));

        // Signature
        Paragraph sig = new Paragraph("_____________________\nAuthorized Signature\nSkill Forge System", normalFont);
        sig.setAlignment(Element.ALIGN_CENTER);
        doc.add(sig);

        doc.close();

        return filename; // 3ashan a3arf saved feen
    }

    public static String getPath(String certID) {
        return FOLDER + certID + ".pdf";
    }

    public static boolean exists(String certID) {
        return new File(getPath(certID)).exists();
    }
}
