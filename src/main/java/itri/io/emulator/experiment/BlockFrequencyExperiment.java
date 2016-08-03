package itri.io.emulator.experiment;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.gen.FakeFileInfo;
import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.Record;

import java.awt.Dimension;
import java.util.Arrays;
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
  }

  @Override
  protected void preProcess(Object obj) {
    String[] splited = (String[]) obj;
    FakeFileInfo fake = new FakeFileInfo(splited, info);
    if (fileMaxSize.containsKey(fake.getFileName())) {
      fileMaxSize.get(fake).updateSize(fake.getSize());
    } else {
      fileMaxSize.put(fake.getFileName(), fake.getSize());
    }
  }

  @Override
  protected void process(Object obj) {
    Record record = (Record) obj;
    if (fileBlocksManager == null) {
      fileBlocksManager = new HashMap<>(fileMaxSize.size());
      initial();
    }
    BlocksManager manager = fileBlocksManager.get(record.getFileName());
    if (manager == null) {
      System.err.print(record.getFileName()
          + " doesn't have blocks? Please check after termination");
      return;
    }
    manager.updateBlocksFrequency(record.getOffset(), record.getLength());
    allSize += manager.getBlocksSize();
  }

  private void initial() {
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      BlocksManager manager = new BlocksManager(entry.getValue());
      fileBlocksManager.put(entry.getKey(), manager);
    }
  }

  @Override
  protected void postProcess() {
    Block[] blocks = new Block[allSize];
    int copyIndex = 0;
    int copyLength = 0;
    long allFrequency = 0;
    for (Map.Entry<FileName, BlocksManager> entry : fileBlocksManager.entrySet()) {
      copyLength = entry.getValue().getBlocksSize();
      allFrequency = entry.getValue().getBlocksFrequency();
      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
      copyIndex += copyLength;
    }
    if (copyLength != allSize) {
      System.err.println("Something wrong within blockes copy process\n."
          + "This is for debug usage. Please comment out when put into production");
    }
    Arrays.sort(blocks);
    BlockFrequencyBarChat bfBarChat =
        new BlockFrequencyBarChat(EXPERIMENT_TITLE, blocks, allFrequency);
    bfBarChat.show();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg.getClass() == String[].class) {
      preProcess(arg);
    } else if (arg.getClass() == Record.class) {
      process(arg);
    } else {
      postProcess();
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
      chartPanel.setMouseWheelEnabled(true);
      chartPanel.setPreferredSize(new Dimension(500, 270));
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
        while (percentageFreq < (int) (allFrequency * percentage)) {
          percentageFreq += blocks[blockIndex++].getFrequency();
        }
        nums[i] = blockIndex;
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

    public void show() {
      this.pack();
      RefineryUtilities.centerFrameOnScreen(this);
      this.setVisible(true);
    }
  }
}
