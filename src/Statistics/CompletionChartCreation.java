package Statistics;

import CustomDataTypes.Course;
import CustomDataTypes.Progress;
import CustomDataTypes.Student;
import DataManagment.UserDatabaseManager;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

//Dah bt3 el Student btw
public class CompletionChartCreation extends SuperChart {

    public CompletionChartCreation(String header, JFreeChart chart) {
        super(header, chart);
    }

    @Override
    protected void drawDataSet(DefaultCategoryDataset dataset, Course course, UserDatabaseManager userDB) {
        for (String studID : course.getStudentIDs()) {
            Student s = (Student) userDB.getRecordByID(studID);
            if (s != null) {
                Progress p = s.getProgressTrackerByCourseID(course.getID());
                if (p != null) {
                    dataset.addValue(p.getCompletionPercentage(), "Completion", s.getUsername());
                }
            }
        }
    }
}
