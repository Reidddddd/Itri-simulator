package itri.io.emulator.experiment;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.gen.FakeFileInfo;
import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.Record;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.jfree.ui.ApplicationFrame;

public class BlockFrequencyExperiment extends Experiment {
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
    for (Map.Entry<FileName, BlocksManager> entry : fileBlocksManager.entrySet()) {
      copyLength = entry.getValue().getBlocksSize();
      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
      copyIndex += copyLength;
    }
    if (copyLength != allSize) {
      System.err.println("Something wrong within blockes copy process\n."
          + "This is for debug usage. Please comment out when put into production");
    }
    Arrays.sort(blocks);
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

  private class BlockFrequencyBarChar extends ApplicationFrame {
    private static final long serialVersionUID = 5751920011962952961L;

    public BlockFrequencyBarChar(String title) {
      super(title);
    }
    
  }
}
