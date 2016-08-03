package itri.io.emulator.experiment;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.experiment.ExperimentsManager.ExperimentState;
import itri.io.emulator.experiment.ExperimentsManager.Tuple;
import itri.io.emulator.gen.FakeFileInfo;
import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.Record;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BlockFrequencyExperiment extends Experiment {
  private final static String EXPERIMENT_TITLE = "Block Frequency";
  private final static int INITIAL_CAPACITY = 200;
  private final static float LOAD_FACTOR = 0.75f;

  private int allSize;
  private Map<FileName, FileSize> fileMaxSize;
  private Map<FileName, BlocksManager> fileBlocksManager;
  private IndexInfo info;

  public BlockFrequencyExperiment(IndexInfo info) {
    this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
    this.info = info;
    this.allSize = 0;
    this.fileBlocksManager = null;
  }

  @Override
  protected void preProcess(Object obj) {
    String[] splited = (String[]) obj;
    FakeFileInfo fake = new FakeFileInfo(splited, info);
    if (fileMaxSize.containsKey(fake.getFileName())) {
      fileMaxSize.get(fake.getFileName()).updateSize(fake.getFileSize());
    } else {
      fileMaxSize.put(fake.getFileName(), fake.getFileSize());
    }
  }

  @Override
  protected void process(Object obj) {
    String[] splited = (String[]) obj;
    Record record = new Record(splited, info);
    if (fileBlocksManager == null) {
      fileBlocksManager = new HashMap<>(fileMaxSize.size());
      initial();
    }
    BlocksManager manager = fileBlocksManager.get(record.getFName());
    manager.updateBlocksFrequency(record.getOffset(), record.getLength());
  }

  private void initial() {
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      BlocksManager manager = new BlocksManager(entry.getValue());
      fileBlocksManager.put(entry.getKey(), manager);
    }
  }

  @Override
  protected void postProcess() {
    for (Map.Entry<FileName, BlocksManager> entry : fileBlocksManager.entrySet()) {
      allSize += entry.getValue().getBlocksSize();
    }
    Block[] blocks = new Block[allSize];
    int copyIndex = 0;
    int copyLength = 0;
    long allFrequency = 0;
    System.out.println(allSize);
    for (Map.Entry<FileName, BlocksManager> entry : fileBlocksManager.entrySet()) {
      copyLength = entry.getValue().getBlocksSize();
      allFrequency += entry.getValue().getBlocksFrequency();
      System.out.println("Copy from " + copyIndex + " to " + (copyIndex + copyLength));
      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
      copyIndex += copyLength;
    }
    if (copyIndex != allSize) {
      System.err.println("Something wrong within blockes copy process.\n"
          + "This is for debug usage. Please comment out when put into production");
      System.out.println("Copy Index is " + copyIndex);
      System.exit(0);
    }
    Arrays.sort(blocks, Collections.reverseOrder());
    System.out.println("Blocks number: " + blocks.length);
    System.out.println("Total Frequency: " + allFrequency);
    BlockFrequencyBarChat bfBarChat =
        new BlockFrequencyBarChat(EXPERIMENT_TITLE, blocks, allFrequency);
    bfBarChat.showGraph();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg == null) {
      postProcess();
    } else if (arg.getClass() == Tuple.class) {
      Tuple tuple = (Tuple) arg;
      if (tuple.getState() == ExperimentState.PREPROCESS) {
        preProcess(tuple.getSplited());
      } else if (tuple.getState() == ExperimentState.PROCESS) {
        process(tuple.getSplited());
      }
    }
  }

  private class BlockFrequencyBarChat extends ApplicationFrame {
    private static final long serialVersionUID = 5751920011962952961L;
    private static final String CATEGORY_LABEL = "Percentage of Block Frequency";
    private static final String VALUE_LABEL = "Block Number";

    public BlockFrequencyBarChat(String title, Block[] blocks, long allFrequency) {
      super(title);
      CategoryDataset dataset = createDataset(blocks, allFrequency);
      JFreeChart chart = createChart(dataset);
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setFillZoomRectangle(true);
      chartPanel.setMouseWheelEnabled(false);
      // chartPanel.setPreferredSize(new Dimension(500, 270));
      setContentPane(chartPanel);
    }

    private JFreeChart createChart(CategoryDataset dataset) {
      JFreeChart chart =
          ChartFactory.createBarChart(EXPERIMENT_TITLE, CATEGORY_LABEL, VALUE_LABEL, dataset);
      return chart;
    }

    private CategoryDataset createDataset(Block[] blocks, long allFrequency) {
      int[] blockNums = createBlockNumbers(blocks, allFrequency);
      String[] categories = createCategories();

      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      for (int i = 0; i < 100; i++) {
        dataset.addValue(blockNums[i], "", categories[i]);
      }
      return dataset;
    }

    private int[] createBlockNumbers(Block[] blocks, long allFrequency) {
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
        System.out.println("Block number: " + nums[i] + " percentage: " + percentage + " count: " + percentageFreq);
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
