package DataManagment;

import CustomDataTypes.Chapter;
import CustomDataTypes.Course;
import CustomDataTypes.Lesson;
import CustomDataTypes.User;

public class GenerationID {
    private UserDatabaseManager userDatabase = UserDatabaseManager.getDatabaseInstance();
    private CourseDatabaseManager courseDatabase = CourseDatabaseManager.getDatabaseInstance();

    public GenerationID() {
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

    // Generate CustomDataTypes.Chapter ID
    public String generateChapterID() {
        int highest = getHighestChapterID();
        return String.format("CH%04d", highest + 1);
    }

    // Generate Lesson ID
    public String generateLessonID() {
        int highest = getHighestLessonID();
        return String.format("L%04d", highest + 1);
    }

    // Get highest chapter ID
    private int getHighestChapterID() {
        int highest = 0;
        for (Course course : courseDatabase.getRecords()) {
            for (Chapter chapter : course.getChapters()) {
                String chapterId = chapter.getChapterID();
                try {
                    String numberPart = chapterId.replaceAll("[^0-9]", "");
                    int idNumber = Integer.parseInt(numberPart);
                    if (idNumber > highest) {
                        highest = idNumber;
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing chapter ID: " + e.getMessage());
                }
            }
        }
        return highest;
    }

    // Get highest lesson ID
    private int getHighestLessonID() {
        int highest = 0;
        for (Course course : courseDatabase.getRecords()) {
            for (Chapter chapter : course.getChapters()) {
                for (Lesson lesson : chapter.getLessons()) {
                    String lessonId = lesson.getLessonID();
                    try {
                        String numberPart = lessonId.replaceAll("[^0-9]", "");
                        int idNumber = Integer.parseInt(numberPart);
                        if (idNumber > highest) {
                            highest = idNumber;
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing lesson ID: " + e.getMessage());
                    }
                }
            }
        }
        return highest;
    }
}
