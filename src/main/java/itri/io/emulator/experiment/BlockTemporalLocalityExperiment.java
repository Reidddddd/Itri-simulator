package itri.io.emulator.experiment;

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
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;
import itri.io.emulator.flusher.FakeFileInfo;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;
import itri.io.emulator.parameter.Record;

public class BlockTemporalLocalityExperiment extends GraphExperiment {
	private final static String EXPERIMENT_TITLE = "Blocks Read Temporal Locality";
	private final static int INITIAL_CAPACITY = 200;
	private final static float LOAD_FACTOR = 0.75f;

	private int allSize;
	private Map<FileName, FileSize> fileMaxSize;
	private Map<FileName, BlockTemporalLocalityManager> fileBlocksManager;

	public BlockTemporalLocalityExperiment() {
		this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
		this.fileBlocksManager = null;
		this.allSize = 0;
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
	  manager.updateBlocksTemporalLocality(record.getOffset(),record.getLength(),Long.valueOf(record.getRanger().getPreOpTime(false)));
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
		private static final String CATEGORY_LABEL = "Avg access time";
	    private static final String VALUE_LABEL = "Block Number";
	    
		public BlockTemporalLocalityBarChat(String title,BlockWithTemporalLocality[] blocks) {
			super(title);
			CategoryDataset dataset = createDataset(blocks);
		    JFreeChart chart = createChart(dataset);
		    ChartPanel chartPanel = new ChartPanel(chart);
		    chartPanel.setFillZoomRectangle(true);
		    chartPanel.setMouseWheelEnabled(false);
		    setContentPane(chartPanel);
//		    ChartUtilities.saveChartAsJPEG
		}
		private JFreeChart createChart(CategoryDataset dataset) {
		      JFreeChart chart =
		          ChartFactory.createBarChart(EXPERIMENT_TITLE, CATEGORY_LABEL, VALUE_LABEL, dataset);
		      return chart;
		}
		
		private CategoryDataset createDataset(BlockWithTemporalLocality[] blocks) {
		      int[] blockNums = createBlockNumbers(blocks);
		      String[] categories = createCategories(blocks);

		      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		      for (int i = 0; i < 50; i++) {
		        dataset.addValue(blockNums[i], "", categories[i]);
		      }
		      return dataset;
		}
		private int[] createBlockNumbers(BlockWithTemporalLocality[] blocks) {
			  long maxAvgIntervalTime = blocks[blocks.length-1].getAvgIntervalTime();
			  int [] nums = new int[50];
			  System.out.println("maxAvgIntervalTime: "+maxAvgIntervalTime);
			  System.out.println("allSize: "+blocks.length);
		      long percentage = 0;
		      int blockIndex = 0;
		      int currentNum = 0;
		      while (blocks[blockIndex].getAvgIntervalTime() == 0){
//		    	  if (blocks[blockIndex].getAccessCount() == 1)
//		    		  System.out.println("*****"+blocks[blockIndex]);
		    	  blockIndex++;
		      }
		      
		      System.out.println(blockIndex);
		      System.out.println(blocks[4321026]);
		      
		      System.out.println(blocks[blockIndex]);
		      System.out.println(blocks[blockIndex+1]);
		      System.out.println(blocks[blockIndex+20000]);
		      System.out.println(blocks[blockIndex+50000]);
		      System.out.println(blocks[blockIndex+1000000]);
		      for ( int i = 0; i < 50; i++){
		    	  percentage += 2;
//		    	  System.out.println(maxAvgIntervalTime * percentage/100);
		    	  while (blocks[blockIndex++].getAvgIntervalTime() <= (maxAvgIntervalTime * percentage / 100 / (1000*60))){
		    		  if (blockIndex < blocks.length)
		    			  currentNum++;
		    		  else
		    			  break;
		    	  }
		    	  nums[i] = currentNum;
		      }
		      return nums;
		}
		
		private String[] createCategories(BlockWithTemporalLocality[] blocks) {
			  long maxAvgIntervalTime = blocks[blocks.length-1].getAvgIntervalTime();
		      String[] categories = new String[50];
		      long percentage = 0;
		      
		      
		      for (int i = 0; i < 50; i++) {
		    	percentage += 2;
		        categories[i] = ""+maxAvgIntervalTime * percentage / 100 / (1000 * 60);
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
