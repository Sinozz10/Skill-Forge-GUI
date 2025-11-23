package Statistics;

import CustomDataTypes.*;
import DataManagment.UserDatabaseManager;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;

public class ChartStatistics extends JFrame {
    public ChartStatistics(String title, JFreeChart chart) {
        super(title);
        setContentPane(new ChartPanel(chart));
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    //Complete Course.
    public static JFreeChart createCompletionChart(Course course, UserDatabaseManager userDB) {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        for (String sid : course.getStudentIDs()) {
            Student s = (Student) userDB.getRecordByID(sid);
            if (s != null) {
                Progress p = s.getProgressTrackerByCourseID(course.getID());
                if (p != null) {
                    data.addValue(p.getCompletionPercentage(), "Completion", s.getUsername());
                }
            }
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Student Completion", "Students", "Completion %",
                data, PlotOrientation.VERTICAL, false, true, false
        );

        customizeChart(chart);
        return chart;
    }

    // Lesson completion chart
    public static JFreeChart createLessonChart(Course course, UserDatabaseManager userDB) {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        for (Chapter ch : course.getChapters()) {
            for (Lesson l : ch.getLessons()) {
                int completed = 0;
                for (String sid : course.getStudentIDs()) {
                    Student s = (Student) userDB.getRecordByID(sid);
                    if (s != null) {
                        Progress progress = s.getProgressTrackerByCourseID(course.getID());
                        if (progress != null && progress.getTrackerByID(l.getLessonID()).isComplete()) {
                            completed++;
                        }
                    }
                }
                double rate = course.getStudentIDs().size() > 0 ? (completed * 100.0 / course.getStudentIDs().size()) : 0;
                data.addValue(rate, "Completion", l.getTitle());
            }
        }
        JFreeChart chart = ChartFactory.createBarChart("Lesson Completion Rates", "Lessons", "%",
                data, PlotOrientation.VERTICAL, false, true, false);

        customizeChart(chart);
        return chart;
    }

    private static void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(new Color(128, 131, 131));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(128, 131, 131));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setMaximumBarWidth(0.05);
        renderer.setSeriesPaint(0, new Color(39, 85, 117));

        // Make axis labels larger
        plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 16)); // X-axis label
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14)); // X-axis values

        plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 16)); // Y-axis label
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14)); // Y-axis values

        plot.getDomainAxis().setLabelPaint(new Color(0, 0, 0));
        plot.getDomainAxis().setTickLabelPaint(new Color(0, 0, 0));
        plot.getRangeAxis().setLabelPaint(new Color(0, 0, 0));
        plot.getRangeAxis().setTickLabelPaint(new Color(0, 0, 0));

        plot.getDomainAxis().setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.UP_45
        );
    }
}
