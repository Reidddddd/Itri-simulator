package itri.io.emulator.para;

import org.apache.commons.lang.StringUtils;

public class ProcThrd {
  private String pid;
  private String tid;

  public ProcThrd(String procThrd) {
    String[] pt = StringUtils.split(procThrd, ".");
    long pidParsed = Long.parseLong(pt[0], 16);
    long tidParsed = Long.parseLong(pt[1], 16);
    pid = String.valueOf(pidParsed);
    tid = String.valueOf(tidParsed);
  }
  
  public static String getPid(String procThrd) {
    String pStr = StringUtils.split(procThrd, ".")[0];
    long processId = Long.parseLong(pStr, 16);
    return String.valueOf(processId);
  }
  
  public static String getTid(String procThrd) {
    String tStr = StringUtils.split(procThrd, ".")[1];
    long threadId = Long.parseLong(tStr, 16);
    return String.valueOf(threadId);
  }
  
  public String getPid() {
    return pid;
  }
  
  public String getTid() {
    return tid;
  }
  
  @Override
  public String toString() {
    return pid + ":" + tid;
  }
}
