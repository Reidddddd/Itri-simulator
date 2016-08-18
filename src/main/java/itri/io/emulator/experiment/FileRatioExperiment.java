package itri.io.emulator.experiment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.common.SortMapTools;
import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.Record;

public class FileRatioExperiment extends GraphExperiment {
	private final static int INITIAL_CAPACITY = 200;
	private final static float LOAD_FACTOR = 0.75f;
	
	private Map<FileName,Long> fileWithBytes;
	private String ratio_experiment_title;
	private String experimentOutputPath;
	private long totalBytes;
	
	public FileRatioExperiment(ExperimentSign sign,String exptOutputPath){
		fileWithBytes = new HashMap(INITIAL_CAPACITY, LOAD_FACTOR);
		ratio_experiment_title="abc";
		experimentOutputPath = exptOutputPath;
		totalBytes = 0;
		switch (sign){
		case READ:
			ratio_experiment_title = "Read Ratio";
			break;
		case WRITE:
			ratio_experiment_title = "Write Ratio";
			break;
		default:
			break;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
	  Tuple tuple = (Tuple) arg;
	  if (tuple.getState() == ExperimentState.PRE_PROCESS) {
	    preProcess(tuple.getRecord());
	  } else if (tuple.getState() == ExperimentState.PROCESS) {
	    process(tuple.getRecord());
	  } else if (tuple.getState() == ExperimentState.POST_PROCESS) {
	    postProcess();
	  }
	}

	@Override
	protected void preProcess(Object obj) {

	}

	@Override
	protected void process(Object obj) {
		CSVRecord csvRecord = (CSVRecord) obj;
	    for (Filter filter : processFilters) {
	      if (!filter.filter(csvRecord)) return;
	    }
	    Record record = new Record(csvRecord);
	    FileName fileName = record.getFName();
	    if (fileWithBytes.containsKey(fileName)){
	        Long currentBytes=fileWithBytes.get(fileName);
	        fileWithBytes.put(fileName, currentBytes + record.getLength());
	    }else{
	    	fileWithBytes.put(fileName, (long) record.getLength());
	    }
	    totalBytes += record.getLength();
	}

	@Override
	protected void postProcess() {
		FilePieChart filePieChart = new FilePieChart(ratio_experiment_title,fileWithBytes,totalBytes);
		filePieChart.showGraph();

	}
	
	private class FilePieChart extends ApplicationFrame{
		  private static final double THRESHOLD = 0.95;
		  public FilePieChart(String title,Map<FileName,Long> fileWithBytes,long totalBytes){
			  super(title);
			  PieDataset dataset = createDataset(fileWithBytes,totalBytes);
		      JFreeChart chart = createChart(dataset);
		      ChartPanel chartPanel = new ChartPanel(chart);
		      chartPanel.setFillZoomRectangle(true);
		      chartPanel.setMouseWheelEnabled(false);
		      setContentPane(chartPanel);
		      try {
		    	File file = new File(experimentOutputPath+File.separator+ratio_experiment_title+".jpg");
				ChartUtilities.saveChartAsJPEG(file, chart, 800, 800);
			  } catch (IOException e) {
				e.printStackTrace();
			  }
		  }
		  private PieDataset createDataset(Map<FileName,Long> fileWithBytes,long totalBytes) {
			  DefaultPieDataset dataset = new DefaultPieDataset( );
			  Map<String,Double> data = createData(fileWithBytes,totalBytes);
			  for (Map.Entry<String, Double> entry : data.entrySet()){
				  dataset.setValue(entry.getKey(), entry.getValue());
			  }
			  
			  return dataset;
		  }
		  private JFreeChart createChart(PieDataset dataset) {
		      JFreeChart chart = ChartFactory.createPieChart(ratio_experiment_title, dataset, true, false, false);
		      PiePlot pieplot = (PiePlot) chart.getPlot();
		      DecimalFormat df = new DecimalFormat("0.00%");//获得一个DecimalFormat对象，主要是设置小数问题,表示小数点后保留两位。  
		      NumberFormat nf = NumberFormat.getNumberInstance();//获得一个NumberFormat对象  
		      StandardPieSectionLabelGenerator sp = new StandardPieSectionLabelGenerator(  
		              "{0}:{2}", nf, df);//获得StandardPieSectionLabelGenerator对象,生成的格式，{0}表示section名，{1}表示section的值，{2}表示百分比。可以自定义  
		      pieplot.setLabelGenerator(sp);//设置饼图显示百分比
		      pieplot.setLegendLabelGenerator(sp);
		      return chart;
		  }
		  public void showGraph() {
		      this.pack();
		      RefineryUtilities.centerFrameOnScreen(this);
		      this.setVisible(true);
		  }
		  public Map<String,Double> createData(Map<FileName,Long> fileWithBytes,long totalBytes){
			  double currentPercent = 0.0;
			  Map<String,Double> data = new HashMap();
			  Map<FileName,Long> sorted = SortMapTools.sortByValue(fileWithBytes);
			  int num = 0;
			  for (Map.Entry<FileName, Long> entry : sorted.entrySet()){
				  System.out.println(entry.getKey().getFileName()+" "+entry.getValue() );
				  long fileBytes = entry.getValue();
				  double percent = (double)fileBytes / totalBytes; 
				  currentPercent  += percent; 
				  data.put(entry.getKey().getFileName(), percent);
				  num++;
				  if ( currentPercent >= THRESHOLD || num > 8)
					  break;
				  
			  }
			  data.put("other all", 1 - currentPercent);
			  
			  return data;
		  }
	  }
}
