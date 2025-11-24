package Statistics;

import CustomDataTypes.*;
import DataManagment.UserDatabaseManager;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class LessonChartCreation extends SuperChart {

    public LessonChartCreation(String header, JFreeChart chart) {
        super(header, chart);
    }

    @Override
    protected void drawDataSet(DefaultCategoryDataset dataset, Course course, UserDatabaseManager userDB) {
        for (Chapter chap : course.getChapters()) {
            for (Lesson lesson : chap.getLessons()) {
                int completed = 0;
                for (String sid : course.getStudentIDs()) {
                    Student s = (Student) userDB.getRecordByID(sid);
                    if (s != null) {
                        Progress progress = s.getProgressTrackerByCourseID(course.getID());
                        if (progress != null) {
                            LessonTracker lt = progress.getTrackerByLessonID(lesson.getLessonID());
                            if (lt != null && lt.isTrue()) {
                                completed++;
                            }
                        }
                    }
                }
                double rate = !course.getStudentIDs().isEmpty() ? rounder((completed * 100.0) / course.getStudentIDs().size()) : 0;

                dataset.addValue(rate, "Completion", lesson.getTitle());
            }
        }
    }
}