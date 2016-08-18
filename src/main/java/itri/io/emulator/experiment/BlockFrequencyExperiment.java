package itri.io.emulator.experiment;

import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.common.SortMapTools;
import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;
import itri.io.emulator.flusher.FakeFileInfo;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;
import itri.io.emulator.parameter.Record;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BlockFrequencyExperiment extends GraphExperiment {
  private final static String EXPERIMENT_TITLE = "Blocks Read Frequency";
  private final static int INITIAL_CAPACITY = 200;
  private final static float LOAD_FACTOR = 0.75f;
  
  private String frequency_experiment_title;
//  private String ratio_experiment_title;
  private String experimentOutputPath;
  
  private int allSize;
  private Map<FileName, FileSize> fileMaxSize;
  private Map<FileName, BlockFrequencyManager> fileBlocksManager;
  
  
  public BlockFrequencyExperiment(ExperimentSign sign,String exptOutputPath) {
    this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
    this.allSize = 0;
    this.fileBlocksManager = null;
    switch (sign){
    case READ:
    	frequency_experiment_title ="Blocks Read Frequency";
//    	ratio_experiment_title = "Read Ratio";
    	break;
    case WRITE:
    	frequency_experiment_title ="Blocks Write Frequency";
//    	ratio_experiment_title = "Write Ratio";
    	break;
    default:
    	break;
    }
    experimentOutputPath = exptOutputPath;
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
    BlockFrequencyManager manager = fileBlocksManager.get(record.getFName());
    manager.updateBlocksFrequency(record.getOffset(), record.getLength());
  }

  private void initial() {
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      BlockFrequencyManager manager = new BlockFrequencyManager(entry.getValue());
      fileBlocksManager.put(entry.getKey(), manager);
    }
  }

  @Override
  protected void postProcess() {
    for (Map.Entry<FileName, BlockFrequencyManager> entry : fileBlocksManager.entrySet()) {
      allSize += entry.getValue().getBlocksSize();
    }
    BlockWithFrequency[] blocks = new BlockWithFrequency[allSize];
    System.out.println("allSize: " + allSize);
    int copyIndex = 0;
    int copyLength = 0;
    long allFrequency = 0;
    for (Map.Entry<FileName, BlockFrequencyManager> entry : fileBlocksManager.entrySet()) {
      copyLength = entry.getValue().getBlocksSize();
//      allFrequency += entry.getValue().getBlocksFrequency();
      allFrequency += entry.getValue().getTotalFrequency();
      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
      copyIndex += copyLength;
    }
    Arrays.sort(blocks, Collections.reverseOrder());
    /** Two prints are for debug usage. Please comment them out when put into production **/
    System.out.println("Blocks number: " + blocks.length);
    System.out.println("Total read frequency: " + allFrequency);
    BlockFrequencyBarChat bfBarChat =
        new BlockFrequencyBarChat(frequency_experiment_title, blocks, allFrequency);
    bfBarChat.showGraph();
    
//    FilePieChart filePieChart = new FilePieChart(ratio_experiment_title,fileBlocksManager,allFrequency);
//    filePieChart.showGraph();
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
  
//  private class FilePieChart extends ApplicationFrame{
//	  private static final double THRESHOLD = 0.95;
//	  public FilePieChart(String title,Map<FileName, BlockFrequencyManager> fileBlocksManager,long allFrequency){
//		  super(title);
//		  PieDataset dataset = createDataset(fileBlocksManager,allFrequency);
//	      JFreeChart chart = createChart(dataset);
//	      ChartPanel chartPanel = new ChartPanel(chart);
//	      chartPanel.setFillZoomRectangle(true);
//	      chartPanel.setMouseWheelEnabled(false);
//	      setContentPane(chartPanel);
//	      try {
//	    	File file = new File(experimentOutputPath+File.separator+ratio_experiment_title+".jpg");
//			ChartUtilities.saveChartAsJPEG(file, chart, 800, 800);
//		  } catch (IOException e) {
//			e.printStackTrace();
//		  }
//	  }
//	  private PieDataset createDataset(Map<FileName, BlockFrequencyManager> fileBlocksManager,long allFrequency) {
//		  DefaultPieDataset dataset = new DefaultPieDataset( );
//		  Map<String,Double> data = createData(fileBlocksManager,allFrequency);
//		  for (Map.Entry<String, Double> entry : data.entrySet()){
//			  dataset.setValue(entry.getKey(), entry.getValue());
//		  }
//		  
//		  return dataset;
//	  }
//	  private JFreeChart createChart(PieDataset dataset) {
//	      JFreeChart chart = ChartFactory.createPieChart(ratio_experiment_title, dataset, true, false, false);
//	      PiePlot pieplot = (PiePlot) chart.getPlot();
//	      DecimalFormat df = new DecimalFormat("0.00%");//获得一个DecimalFormat对象，主要是设置小数问题,表示小数点后保留两位。  
//	      NumberFormat nf = NumberFormat.getNumberInstance();//获得一个NumberFormat对象  
//	      StandardPieSectionLabelGenerator sp = new StandardPieSectionLabelGenerator(  
//	              "{0}:{2}", nf, df);//获得StandardPieSectionLabelGenerator对象,生成的格式，{0}表示section名，{1}表示section的值，{2}表示百分比。可以自定义  
//	      pieplot.setLabelGenerator(sp);//设置饼图显示百分比
//	      pieplot.setLegendLabelGenerator(sp);
//	      return chart;
//	  }
//	  public void showGraph() {
//	      this.pack();
//	      RefineryUtilities.centerFrameOnScreen(this);
//	      this.setVisible(true);
//	  }
//	  public Map<String,Double> createData(Map<FileName, BlockFrequencyManager> fileBlocksManager,long allFrequency){
//		  double currentPercent = 0.0;
//		  Map<String,Double> data = new HashMap();
//		  Map<FileName,BlockFrequencyManager> sorted = SortMapTools.sortByValue(fileBlocksManager);
//		  int num = 0;
//		  for (Map.Entry<FileName, BlockFrequencyManager> entry : sorted.entrySet()){
//			  System.out.println(entry.getKey().getFileName()+" "+entry.getValue().getTotalFrequency() );
//			  long totalFrequency = entry.getValue().getTotalFrequency();
//			  double percent = (double)totalFrequency / allFrequency; 
//			  currentPercent  += percent; 
//			  data.put(entry.getKey().getFileName(), percent);
//			  num++;
//			  if ( currentPercent >= THRESHOLD || num > 8)
//				  break;
//			  
//		  }
//		  data.put("other all", 1 - currentPercent);
//		  
//		  return data;
//	  }
//  }
  
  
  private class BlockFrequencyBarChat extends ApplicationFrame {
    private static final long serialVersionUID = 5751920011962952961L;
    private static final String CATEGORY_LABEL = "Percentage of Blocks Frequency(%)";
    private static final String VALUE_LABEL = "Block Number (ten thousand)";

    public BlockFrequencyBarChat(String title, BlockWithFrequency[] blocks, long allFrequency) {
      super(title);
      IntervalXYDataset dataset = createDataset(blocks, allFrequency);
      JFreeChart chart = createChart(dataset);
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setFillZoomRectangle(true);
      chartPanel.setMouseWheelEnabled(false);
      setContentPane(chartPanel);
      try {
	    File file = new File(experimentOutputPath+File.separator+frequency_experiment_title+".jpg");
		ChartUtilities.saveChartAsJPEG(file, chart, 1000, 800);
	  } catch (IOException e) {
		e.printStackTrace();
	  }
    }

    private JFreeChart createChart(IntervalXYDataset dataset) {
      JFreeChart chart = ChartFactory.createXYBarChart(frequency_experiment_title,CATEGORY_LABEL,false,VALUE_LABEL,dataset);
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
  	  render.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
  	  render.setBaseItemLabelsVisible(true);
  	  
	  ValueAxis axis = plot.getRangeAxis();
	  ValueAxis x = plot.getDomainAxis();
	  axis.setLowerMargin(0.1);
	  axis.setUpperMargin(0.1);
      return chart;
    }

    private IntervalXYDataset createDataset(BlockWithFrequency[] blocks, long allFrequency) {
      double[] blockNums = createBlockNumbers(blocks, allFrequency);
      int[] categories = createCategories();

//      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//      for (int i = 0; i < 100; i++) {
//        dataset.addValue(blockNums[i], "", categories[i]);
//      }
      XYSeries xyseries = new XYSeries("block number"); //先产生XYSeries 对象
  	  for (int i = 0 ; i < 20 ; i++){
  		xyseries.add(categories[i],blockNums[i]);
  	  }
  
  		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
  		xyseriescollection.addSeries(xyseries);
      return new XYBarDataset(xyseriescollection,4.5d);
    }

    private double[] createBlockNumbers(BlockWithFrequency[] blocks, long allFrequency) {
      int position = 0 ;
      while (blocks[position].getFrequency() != 0 )
    	  position++;
//      System.out.println("position: "+position);
//      System.out.println("position:[0] "+blocks[0].getFrequency());
//      System.out.println("position:[last] "+blocks[blocks.length-1].getFrequency());
//      System.out.println("position:[position+100] "+blocks[position+100].getFrequency());
      DecimalFormat df = new DecimalFormat("0.00");
      double[] nums = new double[20];
      long percentageFreq = 0;
      long percentage = 0;
      int blockIndex = 0;
      for (int i = 0; i < 20; i++) {
        percentage += 5;
        while (percentageFreq <= (int) (allFrequency * percentage / 100)) {
          if (blockIndex < position) {
//        	  if (blockIndex < blocks.length) {
            percentageFreq += blocks[blockIndex++].getFrequency();
          } else {
            break;
          }
        }
//        System.out.println(percentageFreq);
        nums[i] = Double.valueOf(df.format( (blockIndex + 1) /10000.0 ));
      }
//      System.out.println(blockIndex);
      return nums;
    }

    private int[] createCategories() {
      int[] categories = new int[20];
      for (int i = 0; i < 20; i++) {
        categories[i] = (i + 1)*5 ;
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
