package itri.io.emulator.parameter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The executiont time of an operation: post operation time - pre operation time.
 */
public class TimeRanger {
  private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS");

  private String preOp;
  private String pstOp;

  private long preOpTime;
  private long pstOpTime;
  
  private long preOpSysTime;
  private long pstOpSysTime;

  public TimeRanger(String preOp, String pstOp,String preOpSysTime,String pstOpSysTime) {
    this.preOp = preOp;
    this.pstOp = pstOp;
    try {
      this.preOpTime = TIME_FORMAT.parse(preOp).getTime();
      this.pstOpTime = TIME_FORMAT.parse(pstOp).getTime();
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      this.preOpTime = -1l;
      this.pstOpTime = -1l;
    }
    this.preOpSysTime = Long.decode(preOpSysTime);
    this.pstOpSysTime = Long.decode(pstOpSysTime);
    
  }

  public String getPreOpTime(boolean humanReadable) {
    if (humanReadable || preOpTime == -1l) return preOp;
    return String.valueOf(preOpTime);
  }

  public String getPstOpTime(boolean humanReadable) {
    if (humanReadable || pstOpTime == -1l) return pstOp;
    return String.valueOf(pstOpTime);
  }

  public long getExecutionTime() {
    return pstOpTime - preOpTime;
  }
  
  public long getPreOpSysTime(){
	return preOpSysTime;
  }
  public long getPstOpSysTime(){
	return pstOpSysTime;
  }
  @Override
  public String toString() {
    return String.valueOf(pstOpTime - preOpTime);
  }
}
