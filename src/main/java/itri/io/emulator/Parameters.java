package itri.io.emulator;

import javax.naming.InvalidNameException;

import itri.io.emulator.util.Configuration;

public class Parameters {
  // Log generator input
  private final static String LOG_PATH = "emulator.io.log.location";
  private String logPath;
  // Log generator output
  private final static String OUTPUT_DIR = "emulator.replay.log.location";
  private String outDir;

  // Filter
  private final static String GROUP_BY = "emulator.merge.keyword";
  private final static String FILTER_NAME = "emulator.keyword.filter";
  private final static String FILTER_IRP = "emulator.irp.filter";
  private final static String FILTER_OPR = "emulator.opr.filter";
  private final static String FILTER_MAJOR_OP = "emulator.majorop.filter";
  private final static String FILTER_STATUS = "emulator.status.filter";
  private String groupBy;
  private String[] filterNames;
  private String[] irpNames;
  private String[] oprNames;
  private String[] majorNames;
  private String[] statusNames;

  // Log simulator input
  private final static String MODFILE_DIR = "emulator.fake.file.location";
  private String modDir;

  // Generator output buffer size
  private final static String RECORD_SIZE = "emulator.buffer.size";
  private int recordSize;

  public Parameters(Configuration conf) throws InvalidNameException {
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

  public String getIOLogInputLocation() {
    return logPath;
  }

  public String getReplayLogOutputLocation() {
    return outDir;
  }

  public String getMergeKeyWord() {
    return groupBy;
  }

  public String[] getKeyWordNames() {
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

  public String getFakeFilesLocation() {
    return modDir;
  }

  public int getBufferSize() {
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
