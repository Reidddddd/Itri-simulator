package itri.io.emulator.para;

public class ProcThrd {
  private String pid;
  private String tid;

  public ProcThrd(String procThrd) {
    String[] pt = procThrd.split("\\.");
    long pidParsed = Long.parseLong(pt[0], 16);
    long tidParsed = Long.parseLong(pt[1], 16);
    pid = String.valueOf(pidParsed);
    tid = String.valueOf(tidParsed);
  }

  public static String getPid(String procThrd) {
    String pStr = procThrd.split("\\.")[0];
    long processId = Long.parseLong(pStr, 16);
    return String.valueOf(processId);
  }

  public static String getTid(String procThrd) {
    String tStr = procThrd.split("\\.")[1];
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
