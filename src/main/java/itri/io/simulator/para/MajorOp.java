package itri.io.simulator.para;

public class MajorOp {
  private final static String READ = "READ";
  private final static String WRITE = "WRITE";
  private final static String IRP_MJ_READ = "IRP_MJ_READ";
  private final static String IRP_MJ_WRITE = "IRP_MJ_WRITE";
  
  public static boolean isReadOp(String majorOp) {
    return majorOp.contains(IRP_MJ_READ);
  }
  
  public static boolean isRead(String majorOp) {
    return majorOp.contains(READ);
  }
  
  public static boolean isWriteOp(String majorOp) {
    return majorOp.contains(IRP_MJ_WRITE);
  }
  
  public static boolean isWrite(String majorOp) {
    return majorOp.contains(WRITE);
  }
  
  public static boolean isOtherOp(String majorOp) {
    return !majorOp.contains(IRP_MJ_READ) && !majorOp.contains(IRP_MJ_WRITE);
  }
  
  public enum OpType {
    READ,
    
    WRITE,
    
    CREATE,
    
    CLOSE,
    
    OTHER;
  }
}
