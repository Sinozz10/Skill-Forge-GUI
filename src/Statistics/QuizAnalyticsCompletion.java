package Statistics;

import CustomDataTypes.*;
import DataManagment.UserDatabaseManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class QuizAnalyticsCompletion extends SuperChart {

    public QuizAnalyticsCompletion(String title, JFreeChart chart) {
        super(title,chart);
    }

    @Override
    protected void drawDataSet(DefaultCategoryDataset dataset, Course course, UserDatabaseManager userDB) {
        for (Chapter ch : course.getChapters()) {
            for (Lesson l : ch.getLessons()) {
                // EL HESAS ELI FEHA QUIZZES BAS
                if (l.hasQuiz() && l.getQuiz() != null) {
                    int completed = 0;
                    for (String sid : course.getStudentIDs()) {
                        Student s = (Student) userDB.getRecordByID(sid);
                        if (s != null) {
                            Progress progress = s.getProgressTrackerByCourseID(course.getID());
                            if (progress != null) {
                                LessonTracker lt = progress.getTrackerByID(l.getLessonID());
                                if (lt != null && lt.isComplete()) {
                                    completed++;
                                }
                            }
                        }
                    }
                    double rate = course.getStudentIDs().size() > 0 ? (completed * 100.0 / course.getStudentIDs().size()) : 0;
                    dataset.addValue(rate, "Completion", l.getTitle() + " Quiz");
                }
            }
        }
    }
}
