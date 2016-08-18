package itri.io.emulator.experiment;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;
import itri.io.emulator.flusher.FakeFileInfo;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;
import itri.io.emulator.parameter.Record;

public class BlockTemporalLocalityExperiment extends GraphExperiment {
	private final static String EXPERIMENT_TITLE = "Blocks Temporal Locality";
	private final static int INITIAL_CAPACITY = 200;
	private final static float LOAD_FACTOR = 0.75f;

	private int allSize;
	private Map<FileName, FileSize> fileMaxSize;
	private Map<FileName, BlockTemporalLocalityManager> fileBlocksManager;
	private String experimentOutputPath;
	
	public BlockTemporalLocalityExperiment(String exptOutputPath) {
		this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
		this.fileBlocksManager = null;
		this.allSize = 0;
		this.experimentOutputPath = exptOutputPath;
	}

	@Override
	public void update(Observable o, Object arg) {
	  Tuple tuple = (Tuple)arg;
	  if (tuple.getState() == ExperimentState.PRE_PROCESS){
		preProcess(tuple.getRecord());
	  } else if (tuple.getState() == ExperimentState.PROCESS){
		process(tuple.getRecord());
	  } else if (tuple.getState() == ExperimentState.POST_PROCESS){
		postProcess();
	  }
	}

	@Override
	protected void preProcess(Object obj) {
	  CSVRecord record = (CSVRecord) obj;
	  for (Filter filter : preProcessFilters) {
	    if (!filter.filter(record)) return;
	  }
	  FakeFileInfo fake = new FakeFileInfo(record);
	  if (fileMaxSize.containsKey(fake.getFileName())) {
	    fileMaxSize.get(fake.getFileName()).updateSize(fake.getFileSize());
	  } else {
	    fileMaxSize.put(fake.getFileName(), fake.getFileSize());
	  }
	}

	@Override
	protected void process(Object obj) {
	  CSVRecord csvRecord = (CSVRecord) obj;
	  for (Filter filter : processFilters) {
	    if (!filter.filter(csvRecord)) return;
	  }
	  Record record = new Record(csvRecord);
	  if (fileBlocksManager == null) {
	    fileBlocksManager = new HashMap<>(fileMaxSize.size());
	    initial();
	  }
	  BlockTemporalLocalityManager manager = fileBlocksManager.get(record.getFName());
	  manager.updateBlocksTemporalLocality(record.getOffset(),record.getLength(),record.getRanger().getPreOpSysTime());
	}

	private void initial() {
	  for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
		BlockTemporalLocalityManager manager = new BlockTemporalLocalityManager(entry.getValue());
		fileBlocksManager.put(entry.getKey(), manager);
		}
	}
	
	
	@Override
	protected void postProcess() {
		for (Map.Entry<FileName,BlockTemporalLocalityManager> entry : fileBlocksManager.entrySet()){
			allSize += entry.getValue().getBlocksSize();
		}
		BlockWithTemporalLocality[] blocks = new BlockWithTemporalLocality[allSize];
		int copyIndex = 0;
		int copyLength = 0;
		for (Map.Entry<FileName, BlockTemporalLocalityManager> entry : fileBlocksManager.entrySet()) {
		      copyLength = entry.getValue().getBlocksSize();
		      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
		      copyIndex += copyLength;
		}
		Arrays.sort(blocks);
		BlockTemporalLocalityBarChat btlBarChat = new BlockTemporalLocalityBarChat(EXPERIMENT_TITLE,blocks);
		btlBarChat.showGraph();
	
	}
	
	private class BlockTemporalLocalityBarChat extends ApplicationFrame {
		private static final String CATEGORY_LABEL = "Avg access time(s)";
	    private static final String VALUE_LABEL = "Block Number";
	    
		public BlockTemporalLocalityBarChat(String title,BlockWithTemporalLocality[] blocks) {
			super(title);
			IntervalXYDataset dataset = createDataset(blocks);
		    JFreeChart chart = createChart(dataset);
		    ChartPanel chartPanel = new ChartPanel(chart);
		    chartPanel.setFillZoomRectangle(true);
		    chartPanel.setMouseWheelEnabled(false);
		    setContentPane(chartPanel);
		    try {
		    	File file = new File(experimentOutputPath+File.separator+EXPERIMENT_TITLE+".jpg");
				ChartUtilities.saveChartAsJPEG(file, chart, 800, 800);
			  } catch (IOException e) {
				e.printStackTrace();
			 }
		}
		private JFreeChart createChart(IntervalXYDataset dataset) {
		    JFreeChart chart =
		          ChartFactory.createXYBarChart(EXPERIMENT_TITLE, CATEGORY_LABEL,false, VALUE_LABEL, dataset);
		    chart.setTextAntiAlias(false);
	  	    chart.setBorderVisible(false);
	  	    chart.setBackgroundPaint(Color.white);
	  	    chart.getTitle().setFont(new Font("Consolas",Font.PLAIN, 20));
	  	    chart.getLegend().setFrame(new BlockBorder(Color.black));
	  	  
	  	    XYPlot plot = chart.getXYPlot();
	  	    plot.setBackgroundPaint(Color.lightGray);
	  	    plot.setOutlinePaint(Color.black);
	  	    plot.setRangeGridlinesVisible(false);
	  	    plot.setDomainGridlinesVisible(false);
	  	  
	  	    XYBarRenderer render = (XYBarRenderer) plot.getRenderer();
	  	    render.setBarPainter(new StandardXYBarPainter());
	  	    render.setDrawBarOutline(true);
	  	  
		    ValueAxis axis = plot.getRangeAxis();
		    ValueAxis x = plot.getDomainAxis();
		    axis.setLowerMargin(0.1);
		    axis.setUpperMargin(0.1);
		    return chart;
		}
		
		private IntervalXYDataset createDataset(BlockWithTemporalLocality[] blocks) {
		    int[] blockNums = createBlockNumbers(blocks);
		    int[] categories = createCategories(blocks);

//		      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		    XYSeries xyseries = new XYSeries("block number");
		    for (int i = 0; i < blockNums.length; i++) {
		    	xyseries.add(categories[i],blockNums[i]);
		    }
		    XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		    xyseriescollection.addSeries(xyseries);
		    return xyseriescollection;
		}
		private int[] createBlockNumbers(BlockWithTemporalLocality[] blocks) {
			double maxAvgIntervalTime = blocks[blocks.length-1].getAvgIntervalTime();
			  
//			int [] nums = new int[ (int) Math.ceil(maxAvgIntervalTime / 10000.0)];
			int [] nums = new int[ 100];
			System.out.println("maxAvgIntervalTime: "+maxAvgIntervalTime);
			System.out.println("allSize: "+blocks.length);
			System.out.println(nums.length);
		    long percentage = 0;
		    int blockIndex = 0;
		    int currentNum = 0;
		    while (blocks[blockIndex].getAvgIntervalTime() == 0){
//		    	  if (blocks[blockIndex].getAccessCount() == 1)
//		    		  System.out.println("*****"+blocks[blockIndex]);
		    	blockIndex++;
		    }

//		      for ( int i = 0; i < 50; i++){
//		    	  percentage += 2;
////		    	  System.out.println(maxAvgIntervalTime * percentage/100);
//		    	  while (blocks[blockIndex++].getAvgIntervalTime() <= (maxAvgIntervalTime * percentage / 100 / (1000*60))){
//		    		  if (blockIndex < blocks.length)
//		    			  currentNum++;
//		    		  else
//		    			  break;
//		    	  }
//		    	  nums[i] = currentNum;
//		      }
		    for(int i = 0 ; i < nums.length ; i++){
		    	percentage += (1000 * 10000);
		    	  
		    	while ( blockIndex < blocks.length && blocks[blockIndex].getAvgIntervalTime()<= percentage){
		   		  currentNum++;
		    	  blockIndex++;
		    	}
		    	  nums[i] = currentNum;
		    }
		    return nums;
		}
		
		private int[] createCategories(BlockWithTemporalLocality[] blocks) {
			  double maxAvgIntervalTime = blocks[blocks.length-1].getAvgIntervalTime();
			  int [] categories = new int[ 100];
		      int percentage = 0;
		      
		      for (int i = 0; i < categories.length; i++) {
		    	percentage += 1;
		        categories[i] = percentage;
		      }
		      return categories;
		}
		
		public void showGraph() {
		      this.pack();
		      RefineryUtilities.centerFrameOnScreen(this);
		      this.setVisible(true);
		}
	}
}
