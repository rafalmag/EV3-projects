import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.utility.Delay;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart extends JFrame {
	private static final long serialVersionUID = 1L;
	private static String title = "EV3 sampling";
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	private RemoteEV3 ev3;
	private RMISampleProvider sp;
	private String category;
	private String[] labels;
	private float frequency;
	
	public BarChart(String host, String sensorClass, String portName, String category, String[] labels, 
			String units, float minValue, float maxValue, int windowWidth, int windowHeight, float frequency) throws Exception {
		super(title);
		ev3 = new RemoteEV3(host);
		this.category = category;
		this.labels = labels;
		this.frequency = frequency;
		sp = ev3.createSampleProvider(portName, sensorClass, null);
		JFreeChart chart = ChartFactory.createBarChart(title, category, units, dataset, 
				PlotOrientation.VERTICAL, true, true, false);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
		setContentPane(chartPanel);
		
		CategoryPlot plot = chart.getCategoryPlot();
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(minValue, maxValue);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    addWindowListener(new WindowAdapter() {
	    	 
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	try {
					sp.close();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
	        }
	    });
	}

	public void run() throws Exception {
        while(true) {
        	float[] sample = sp.fetchSample();
        	dataset.clear();
        	for(int i=0;i<labels.length;i++) {
        		dataset.addValue(sample[i], labels[i], category);
        	}
    		Delay.msDelay(((int) (1000f/frequency)));
        }
	}
	
	public static void main(String[] args) throws Exception {
		String[] labels = {"X","Y","Z"};
        BarChart demo = new BarChart("192.168.0.9", "lejos.hardware.sensor.MindsensorsAccelerometer", "S1", 
        		                     "Acceleration", labels, "m/s/s", -20, 20, 500, 300, 5);
        demo.pack();
        demo.setVisible(true);
        demo.run();
	}
}
