package itri.io.simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class LogGenerator<K, V> extends Observable {
  private static Log LOG = LogFactory.getLog(LogGenerator.class);
  
  protected static int INITIAL_CAPACITY = 100;
  protected static float LOAD_FACTOR = 0.75F;

  protected IndexInfo info;
  protected String filePath;
  protected BufferedReader reader;
  protected ConditionManager manager;
  
  @SuppressWarnings("rawtypes")
  public static LogGenerator createGenerator(GroupByOption.Option option,
                                             String filePath,
                                             IndexInfo info) {
    switch (option) {
      case FILE_NAME: return new FileBasedLogGenerator(filePath, info);
      default: return new FileBasedLogGenerator(filePath, info);
    }
  }

  public LogGenerator(String filePath, IndexInfo info) {
    this.filePath = filePath;
    this.info = info;
    this.manager = new ConditionManager();
  }
  
  public void generate(Parameters params) {
    try {
      if (open()) {
        groupBy(manager);
        filterOpr(manager);
        filterIrp(manager);
        filterMajorOp(manager);
        filterStatus(manager);
        filterName(manager, params.getFilterNames());
        generate(manager, reader, info);
      }
    } catch (FileNotFoundException e) {
      LOG.error(e.getMessage());
      LOG.error(filePath + " is not found.");
    } finally {
      close();
    }
  }

  public void generate() {
    generate(null);
  }
  
  /**
   * Users should call generate() before getLog(), otherwise you will get nothing.
   * Logs contain those records that passed through user specified conditions.
   * 
   * @return logs 
   */
  public abstract Map<K, V> getLog();
  
  public abstract void flush(String outDir);

  public abstract void generate(ConditionManager manager, BufferedReader reader, IndexInfo info);

  public abstract void groupBy(ConditionManager manager);

  public abstract void filterOpr(ConditionManager manager);
  
  public abstract void filterIrp(ConditionManager manager);

  public abstract void filterMajorOp(ConditionManager manager);

  public abstract void filterStatus(ConditionManager manager);
  
  public abstract void filterName(ConditionManager manager, String[] filterNames);
  
  protected String[] trimedArrays(String line) {
    String[] trimed = StringUtils.split(line, "\t");
    for (int i = 0; i < trimed.length; i++) {
      trimed[i] = StringUtils.trim(trimed[i]);
    }
    return trimed;
  }

  private boolean open() throws FileNotFoundException {
    boolean fileOpenSuccess = true;
    try {
      reader = new BufferedReader(new FileReader(filePath));
    } catch (FileNotFoundException e) {
      LOG.error("Can not find the file in the path: " + filePath);
      fileOpenSuccess = false;
      throw new FileNotFoundException(e.getMessage());
    }
    return fileOpenSuccess;
  }

  private void close() {
    try {
      if (reader != null) reader.close();
    } catch (IOException e) {
      LOG.error("Error occurs when close the file in the path: " + filePath);
    }
  }
}
