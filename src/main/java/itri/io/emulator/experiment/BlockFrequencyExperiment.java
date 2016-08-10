package itri.io.emulator.experiment;

import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;
import itri.io.emulator.flusher.FakeFileInfo;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;
import itri.io.emulator.parameter.Record;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BlockFrequencyExperiment extends GraphExperiment {
  private final static String EXPERIMENT_TITLE = "Blocks Read Frequency";
  private final static int INITIAL_CAPACITY = 200;
  private final static float LOAD_FACTOR = 0.75f;

  private int allSize;
  private Map<FileName, FileSize> fileMaxSize;
  private Map<FileName, BlockFrequencyManager> fileBlocksManager;

  public BlockFrequencyExperiment() {
    this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
    this.allSize = 0;
    this.fileBlocksManager = null;
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
      allFrequency += entry.getValue().getBlocksFrequency();
      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
      copyIndex += copyLength;
    }
    Arrays.sort(blocks, Collections.reverseOrder());
    /** Two prints are for debug usage. Please comment them out when put into production **/
    System.out.println("Blocks number: " + blocks.length);
    System.out.println("Total read frequency: " + allFrequency);
    BlockFrequencyBarChat bfBarChat =
        new BlockFrequencyBarChat(EXPERIMENT_TITLE, blocks, allFrequency);
    bfBarChat.showGraph();
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

  private class BlockFrequencyBarChat extends ApplicationFrame {
    private static final long serialVersionUID = 5751920011962952961L;
    private static final String CATEGORY_LABEL = "Percentage of Blocks Read Frequency";
    private static final String VALUE_LABEL = "Block Number";

    public BlockFrequencyBarChat(String title, BlockWithFrequency[] blocks, long allFrequency) {
      super(title);
      CategoryDataset dataset = createDataset(blocks, allFrequency);
      JFreeChart chart = createChart(dataset);
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setFillZoomRectangle(true);
      chartPanel.setMouseWheelEnabled(false);
      setContentPane(chartPanel);
    }

    private JFreeChart createChart(CategoryDataset dataset) {
      JFreeChart chart =
          ChartFactory.createBarChart(EXPERIMENT_TITLE, CATEGORY_LABEL, VALUE_LABEL, dataset);
      return chart;
    }

    private CategoryDataset createDataset(BlockWithFrequency[] blocks, long allFrequency) {
      int[] blockNums = createBlockNumbers(blocks, allFrequency);
      String[] categories = createCategories();

      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      for (int i = 0; i < 100; i++) {
        dataset.addValue(blockNums[i], "", categories[i]);
      }
      return dataset;
    }

    private int[] createBlockNumbers(BlockWithFrequency[] blocks, long allFrequency) {
      int[] nums = new int[100];
      long percentageFreq = 0;
      float percentage = 0.0f;
      int blockIndex = 0;
      for (int i = 0; i < 100; i++) {
        percentage += 0.01f;
        if (percentage > 0.99f) percentage = 1.0f;
        while (percentageFreq < (int) (allFrequency * percentage)) {
          if (blockIndex < blocks.length) {
            percentageFreq += blocks[blockIndex++].getFrequency();
          } else {
            break;
          }
        }
        nums[i] = blockIndex + 1;
      }
      return nums;
    }

    private String[] createCategories() {
      String[] categories = new String[100];
      for (int i = 0; i < 100; i++) {
        categories[i] = (i + 1) + "%";
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
