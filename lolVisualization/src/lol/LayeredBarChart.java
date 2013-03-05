package lol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.SortOrder;

import twitter4j.Status;

public class LayeredBarChart extends ApplicationFrame
{

	public LayeredBarChart(String s)
	{
		super(s);
		JPanel jpanel = createPanel();
		jpanel.setPreferredSize(new Dimension(1000, 600));
		setContentPane(jpanel);
	}

	private static CategoryDataset createDataset()
	{
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		List<String> champions = Arrays.asList("Amumu", "Cho Gath", "Darius", "Draven", "Garen", "Jarvan", 
				"Jayce", "Karthus", "Katarina", "Lee Sin", "Lux", "Nidalee", 
				"Olaf", "Poppy", "Rengar", "Varus", "Wukong", "Xin");
		
		TwitterDataGenerator dataGenerator = new TwitterDataGenerator(champions);
		Map<String, Map<String, List<Status>>> tweetsByChampion = dataGenerator.getTwitterData();
		
		
		for (String champion : tweetsByChampion.keySet()) {
			Map<String, List<Status>> resultByDate  = (Map<String, List<Status>>)tweetsByChampion.get(champion);
			//System.out.println("----FOR----: "+ champion);
		for (String date : resultByDate.keySet()) {
			//System.out.println("KEY: " + date + " COUNT : "	+ resultByDate.get(date).size());
			defaultcategorydataset.addValue(resultByDate.get(date).size(), champion, date);
			
		}
		}
		return defaultcategorydataset;
	}

	private static JFreeChart createChart(CategoryDataset categorydataset)
	{
		JFreeChart jfreechart = ChartFactory.createBarChart("League of Legends: Champions", "Champions", "# of tweets", categorydataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot categoryplot = (CategoryPlot)jfreechart.getPlot();
		categoryplot.setDomainGridlinesVisible(true);
		categoryplot.setRangePannable(true);
		categoryplot.setRangeZeroBaselineVisible(true);
		NumberAxis numberaxis = (NumberAxis)categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		LayeredBarRenderer layeredbarrenderer = new LayeredBarRenderer();
		layeredbarrenderer.setDrawBarOutline(false);
		categoryplot.setRenderer(layeredbarrenderer);
		categoryplot.setRowRenderingOrder(SortOrder.DESCENDING);
		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
		GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
		GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
		layeredbarrenderer.setSeriesPaint(0, gradientpaint);
		layeredbarrenderer.setSeriesPaint(1, gradientpaint1);
		layeredbarrenderer.setSeriesPaint(2, gradientpaint2);
		categoryplot.setOutlineVisible(true);
		return jfreechart;
	}

	public static JPanel createPanel()
	{
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public static void main(String args[])
	{
		LayeredBarChart LayeredBarChart = new LayeredBarChart("Lol Visualization Chart");
		LayeredBarChart.pack();
		RefineryUtilities.centerFrameOnScreen(LayeredBarChart);
		LayeredBarChart.setVisible(true);
	}
}
