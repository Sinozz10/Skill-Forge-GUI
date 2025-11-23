package CustomDataTypes;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Student extends User {
    @Expose
    private final ArrayList<Progress> progressTrackers = new ArrayList<>();

    @Expose
    private final ArrayList<Certificate> certificates = new ArrayList<>();

    public Student(String uID, String role, String username, String email, String hashedPassword) {
        super(uID, role, username, email, hashedPassword);
    }

    public ArrayList<Progress> getAllProgressTrackers() {
        return progressTrackers;
    }

    public Progress getProgressTrackerByCourseID(String ID) {
        for (Progress prog : progressTrackers) {
            if (prog.getCourseID().equals(ID)) {
                return prog;
            }
        }
        return null;
    }

    public ArrayList<Certificate> getCertificates() {
        if (certificates == null) {
            return new ArrayList<>();
        }
        return certificates;
    }

    public void addCertificate(Certificate cert) {
        certificates.add(cert);
    }

    public Certificate getCertificateByCourseID(String courseID) {
        for (Certificate cert : certificates) {
            if (cert.getCourseID().equals(courseID)) {
                return cert;
            }
        }
        return null;
    }

    @Override
    public void addCourse(Course course) {
        courseIDs.add(course.getID());
        progressTrackers.add(new Progress(course, userID));
    }

    @Override
    public void removeCourse(Course course) {
        courseIDs.remove(course.getID());
        progressTrackers.removeIf(prog -> course.getID().equals(prog.getCourseID()));
    }
}
