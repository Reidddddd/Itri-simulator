package itri.io.simulator.para;

import itri.io.simulator.FilterOption;

public class MajorOp {
  private final static String READ = "READ";
  private final static String WRITE = "WRITE";
  private final static String ALL = "ALL";
  private final static String OTHER = "OTHER";
  private final static String IRP_MJ_READ = "IRP_MJ_READ";
  private final static String IRP_MJ_WRITE = "IRP_MJ_WRITE";

  public static FilterOption.MajorOpOption getMajorOpOption(String majorOp) {
    FilterOption.MajorOpOption majorOption;
    switch (majorOp.toUpperCase()) {
      case READ: majorOption = FilterOption.MajorOpOption.IRP_READ; break;
      case WRITE: majorOption =  FilterOption.MajorOpOption.IRP_WRITE; break;
      case OTHER: majorOption = FilterOption.MajorOpOption.IRP_OTHER; break;
      case ALL: majorOption = FilterOption.MajorOpOption.IRP_ALL; break;
      default: majorOption = FilterOption.MajorOpOption.IRP_ALL; break;
    }
    return majorOption;
  }

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
