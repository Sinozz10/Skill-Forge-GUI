package CustomDataTypes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Certificate {
    private String certificateID;
    private String studentID;
    private String courseID;
    private String courseName;
    private String studentName;
    private Date issuedDate;

    public Certificate(String certificateID, String studentID, String courseID,
    String studentName, String courseName) {
        this.certificateID = certificateID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.studentName = studentName;
        this.courseName = courseName;
        this.issuedDate = new Date();
    }

    public String getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(String certificateID) {
        this.certificateID = certificateID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(issuedDate);
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "issuedDate=" + getFormattedDate() +
                ", courseName='" + courseName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", certificateID='" + certificateID + '\'' +
                '}';
    }
}
