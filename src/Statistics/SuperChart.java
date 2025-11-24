package Statistics;

import CustomDataTypes.Course;
import DataManagment.UserDatabaseManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public abstract class SuperChart extends JFrame {
    public SuperChart(String header, JFreeChart chart) {
        super(header);
        setContentPane(new ChartPanel(chart));
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public JFreeChart createChart(Course course, UserDatabaseManager userDB, String title, String xAxisLabel, String yAxisLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        drawDataSet(dataset, course, userDB);

        JFreeChart chart = ChartFactory.createBarChart(title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
                false, true, false);

        customizeChart(chart);
        return chart;
    }

    protected abstract void drawDataSet(DefaultCategoryDataset dataset, Course course, UserDatabaseManager userDB);

    protected static void customizeChart(JFreeChart chart) {

        chart.setBackgroundPaint(new Color(128, 131, 131));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(128, 131, 131));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setMaximumBarWidth(0.03);
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

    protected static double rounder(double mark) {
        int decimalPlaces = 2;
        // Round to 2 decimal places
        return Math.round(mark * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }
}
