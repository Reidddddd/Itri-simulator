package itri.io.simulator;

import org.apache.hadoop.conf.Configuration;

public class Parameters {
  private final static String LOG_PATH   = "simulator.logpath";
  private final static String GROUP_BY   = "simulator.groupby";
  private final static String MODE       = "simulator.generatelog.mode";
  private final static String OUTPUT_DIR = "simulator.log.output.dir";
  private final static String FILE_TEST = "simulator.test.file";
  private final static String SIMULATE_AFTER_GENLOG = "simulator.simulate.afterlog";
  private final static String RECORD_SIZE = "simulator.record.size";
  
  private String logPath;
  private String groupBy;
  private String mode;
  private String outDir;
  private String fileTst;
  private boolean afterLog;
  private int recordSize;
  
  public Parameters(Configuration conf) {
    logPath = conf.get(LOG_PATH);
    groupBy = conf.get(GROUP_BY);
    mode = conf.get(MODE);
    outDir = conf.get(OUTPUT_DIR);
    fileTst = conf.get(FILE_TEST);
    afterLog = conf.getBoolean(SIMULATE_AFTER_GENLOG, false);
    recordSize = conf.getInt(RECORD_SIZE, 50000);
  }
  
  public String getLogPath() {
    return logPath;
  }
  
  public String getGroupBy() {
    return groupBy;
  }
  
  public String getMode() {
    return mode;
  }
  
  public String getOutDir() {
    return outDir;
  }
  
  public boolean getAfterLog() {
    return afterLog;
  }
  
  public String getFileTest() {
    return fileTst;
  }
  
  public int getRecordSize() {
    return recordSize;
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(logPath + "\n")
           .append(groupBy + "\n")
           .append(mode + "\n")
           .append(outDir + "\n")
           .append(afterLog + "\n")
           .append(fileTst + "\n")
           .append(recordSize + "\n");
    return builder.toString();
  }
}
