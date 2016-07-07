package itri.io.simulator;

import org.apache.hadoop.conf.Configuration;

public class Parameters {
  // Log generator input
  private final static String LOG_PATH = "simulator.logpath";
  private String logPath;
  // Log generator output
  private final static String OUTPUT_DIR = "simulator.log.output.dir";
  private String outDir;

  // Filter
  private final static String GROUP_BY = "simulator.groupby";
  private final static String FILTER_NAME = "simulator.keyword.filter";
  private final static String FILTER_IRP = "simulator.irp.filter";
  private final static String FILTER_OPR = "simulator.opr.filter";
  private final static String FILTER_MAJOR_OP = "simulator.majorop.filter";
  private final static String FILTER_STATUS = "simulator.status.filter";
  private String groupBy;
  private String[] filterNames;
  private String[] irpNames;
  private String[] oprNames;
  private String[] majorNames;
  private String[] statusNames;

  // Log simulator input
  private final static String MODFILE_DIR = "simulator.modfile.dir";
  private String modDir;

  // Generator output buffer size
  private final static String RECORD_SIZE = "simulator.record.size";
  private int recordSize;

  public Parameters(Configuration conf) {
    logPath = conf.get(LOG_PATH);
    outDir = conf.get(OUTPUT_DIR);
    
    groupBy = conf.get(GROUP_BY);
    filterNames = conf.getStrings(FILTER_NAME);
    irpNames = conf.getStrings(FILTER_IRP);
    oprNames = conf.getStrings(FILTER_OPR);
    majorNames = conf.getStrings(FILTER_MAJOR_OP);
    statusNames = conf.getStrings(FILTER_STATUS);

    modDir = conf.get(MODFILE_DIR);

    recordSize = conf.getInt(RECORD_SIZE, 50000);
  }

  public String getLogPath() {
    return logPath;
  }
  
  public String getOutDir() {
    return outDir;
  }

  public String getGroupBy() {
    return groupBy;
  }

  public String[] getFilterNames() {
    return filterNames;
  }

  public String[] getIrpNames() {
    return irpNames;
  }

  public String[] getOprNames() {
    return oprNames;
  }

  public String[] getMajorNames() {
    return majorNames;
  }

  public String[] getStatusNames() {
    return statusNames;
  }

  public String getModDir() {
    return modDir;
  }
  
  public int getRecordSize() {
    return recordSize;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(logPath + "\n").append(outDir + "\n")
           .append(groupBy + "\n").append(irpNames + "\n")
           .append(oprNames + "\n").append(majorNames + "\n")
           .append(statusNames + "\n").append(filterNames + "\n")
           .append(modDir + "\n").append(recordSize + "\n");
    return builder.toString();
  }
}
