package itri.io.emulator;

import itri.io.emulator.para.IrpFlag;
import itri.io.emulator.para.MajorOp;
import itri.io.emulator.para.OprFlag;
import itri.io.emulator.para.Status;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;

public abstract class LogCleaner<K, V> extends Observable {
  protected static int INITIAL_CAPACITY = 100;
  protected static float LOAD_FACTOR = 0.75F;

  protected IndexInfo info;
  protected String filePath;
  protected BufferedReader reader;
  protected ConditionManager manager;

  @SuppressWarnings("rawtypes")
  public static LogCleaner createGenerator(GroupByOption.Option option,
                                             String filePath,
                                             IndexInfo info) {
    LogCleaner log = null;
    switch (option) {
      case FILE_NAME: log = new FileBasedLogCleaner(filePath, info); break;
      case TIME_SEQ: log = new TimeBasedLogCleaner(filePath, info); break;
      default:  log = new FileBasedLogCleaner(filePath, info); break;
    }
    return log;
  }

  public LogCleaner(String filePath, IndexInfo info) {
    this.filePath = filePath;
    this.info = info;
    this.manager = new ConditionManager();
  }

  public void generate(Parameters params) {
    try {
      if (open()) {
        groupBy(manager);
        filterOpr(manager, params.getOprNames());
        filterIrp(manager, params.getIrpNames());
        filterMajorOp(manager, params.getMajorNames());
        filterStatus(manager, params.getStatusNames());
        filterName(manager, params.getKeyWordNames());
        generate(manager, reader, info);
      }
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    } finally {
      close();
    }
  }

  public void generate() {
    generate(null);
  }

  /**
   * Users should call generate() before getLog(), otherwise you will get nothing. Logs contain
   * those records that passed through user specified conditions.
   * @return logs
   */
  public abstract Map<K, V> getLog();

  public abstract void generate(ConditionManager manager, BufferedReader reader, IndexInfo info);

  public abstract void groupBy(ConditionManager manager);

  public void filterOpr(ConditionManager manager, String[] oprs) {
    if (oprs != null && oprs.length != 0) {
      FilterOption.OprOption[] oprOptions = new FilterOption.OprOption[oprs.length];
      for (int i = 0; i < oprs.length; i++) {
        oprOptions[i] = OprFlag.getOprOption(oprs[i]);
      }
      manager.addFilterOprCondition(oprOptions);
    } else {
      FilterOption.OprOption[] oprOptions = { FilterOption.OprOption.IRP };
      manager.addFilterOprCondition(oprOptions);
    }
  }

  public void filterIrp(ConditionManager manager, String[] irps) {
    if (irps != null && irps.length != 0) {
      FilterOption.IrpOption[] irpOptions = new FilterOption.IrpOption[irps.length];
      for (int i = 0; i < irps.length; i++) {
        irpOptions[i] = IrpFlag.getIrpOption(irps[i]);
      }
      manager.addFilterIrpCondition(irpOptions);
    } else {
      FilterOption.IrpOption[] irpOptions = { FilterOption.IrpOption.ALL };
      manager.addFilterIrpCondition(irpOptions);
    }
  }

  public void filterMajorOp(ConditionManager manager, String[] majorOps) {
    if (majorOps != null && majorOps.length != 0) {
      FilterOption.MajorOpOption[] majorOpOptions = new FilterOption.MajorOpOption[majorOps.length];
      for (int i = 0; i < majorOps.length; i++) {
        majorOpOptions[i] = MajorOp.getMajorOpOption(majorOps[i]);
      }
      manager.addFilterMajorOpCondition(majorOpOptions);
    } else {
      FilterOption.MajorOpOption[] majorOpOptions =
          { FilterOption.MajorOpOption.IRP_READ, FilterOption.MajorOpOption.IRP_WRITE };
      manager.addFilterMajorOpCondition(majorOpOptions);
    }
  }

  public void filterStatus(ConditionManager manager, String[] status) {
    if (status != null && status.length != 0) {
      FilterOption.StatusOption[] statusOptions = new FilterOption.StatusOption[status.length];
      for (int i = 0; i < status.length; i++) {
        statusOptions[i] = Status.getStatusOption(status[i]);
      }
      manager.addFilterStatusCondition(statusOptions);
    } else {
      FilterOption.StatusOption[] statusOptions = { FilterOption.StatusOption.SUCCESS };
      manager.addFilterStatusCondition(statusOptions);
    }
  }

  public void filterName(ConditionManager manager, String[] filterNames) {
    if (filterNames != null && filterNames.length != 0) {
      manager.addFilterNameCondition(filterNames);
    }
  }

  protected String[] trimedArrays(String line) {
    String[] trimed = line.split("\t");
    for (int i = 0; i < trimed.length; i++) {
      trimed[i] = trimed[i].trim();
    }
    return trimed;
  }

  protected boolean open() throws FileNotFoundException {
    boolean fileOpenSuccess = true;
    try {
      reader = new BufferedReader(new FileReader(filePath));
    } catch (FileNotFoundException e) {
      System.err.println("Can not find the file in the path: " + filePath);
      fileOpenSuccess = false;
      throw new FileNotFoundException(e.getMessage());
    }
    return fileOpenSuccess;
  }

  protected void close() {
    try {
      if (reader != null) reader.close();
    } catch (IOException e) {
      System.err.println("Error occurs when close the file in the path: " + filePath);
    }
  }
}
