package utils;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartUtil {
	public static String generatePieChart(int passed, int failed, int skipped) {
		try {
			DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Passed", passed);
			dataset.setValue("Failed", failed);
			dataset.setValue("Skipped", skipped);

			JFreeChart chart = ChartFactory.createPieChart("Test Results", dataset, true, true, false);

			org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
			plot.setSectionPaint("Passed", java.awt.Color.GREEN.darker());
			plot.setSectionPaint("Failed", java.awt.Color.RED);
			plot.setSectionPaint("Skipped", java.awt.Color.ORANGE);

			String filePath = "reports/piechart.png";
			ChartUtils.saveChartAsPNG(new File(filePath), chart, 400, 300);

			return filePath;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}