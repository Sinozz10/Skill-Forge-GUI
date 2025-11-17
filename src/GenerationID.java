public class GenerationID {
    private UserDatabaseManager userDatabase;
    private CourseDatabaseManager courseDatabase;

    public GenerationID(UserDatabaseManager userDatabase, CourseDatabaseManager courseDatabase) {
        this.userDatabase = userDatabase;
        this.courseDatabase = courseDatabase;
    }

    public CourseDatabaseManager getCourseDatabase() {
        return courseDatabase;
    }

    public void setCourseDatabase(CourseDatabaseManager courseDatabase) {
        this.courseDatabase = courseDatabase;
    }

    public UserDatabaseManager getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(UserDatabaseManager userDatabase) {
        this.userDatabase = userDatabase;
    }

    // Generate User ID (Student or Instructor)
    public String generateUserID(String role) {
        String prefix = role.equalsIgnoreCase("student") ? "S" : "I";
        int highest = getHighestUserID(role);
        return String.format("%s%04d", prefix, highest + 1);
    }

    // Generate Course ID
    public String generateCourseID() {
        int highest = getHighestCourseID();
        return String.format("C%04d", highest + 1);
    }

    // Get highest user ID for a role
    private int getHighestUserID(String role) {
        int highest = 0;
        for (User user : userDatabase.getRecords()) {
            if (user.getRole().equalsIgnoreCase(role)) {
                String userId = user.getID();
                try {
                    String numberPart = userId.substring(1); // Remove prefix
                    int idNumber = Integer.parseInt(numberPart);
                    if (idNumber > highest) {
                        highest = idNumber;
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing user ID: " + e.getMessage());
                }
            }
        }
        return highest;
    }

    // Get highest course ID
    private int getHighestCourseID() {
        int highest = 0;
        for (Course course : courseDatabase.getRecords()) {
            String courseId = course.getID();
            try {
                String numberPart = courseId.substring(1); // Remove prefix 'C'
                int idNumber = Integer.parseInt(numberPart);
                if (idNumber > highest) {
                    highest = idNumber;
                }
            } catch (Exception e) {
                System.err.println("Error parsing course ID: " + e.getMessage());
            }
        }
        return highest;
    }
}
