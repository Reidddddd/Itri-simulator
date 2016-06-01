package itri.io.simulator.para;

public class OprFlag {
  private final static String IRP = "IRP";
  private final static String FIO = "FIO";
  private final static String FSF = "FSF";
  
  private String orp;
  
  public OprFlag(String opr) {
    switch (opr) {
      case IRP: this.orp = IRP; break;
      case FIO: this.orp = FIO; break;
      case FSF: this.orp = FSF; break;
    }
  }
  
  public static boolean isIrp(String flag) {
    return IRP.equals(flag);
  }
  
  public static boolean isFSF(String flag) {
    return FSF.equals(flag);
  }
  
  public static boolean isFIO(String flag) {
    return FIO.equals(flag);
  }
  
  public String getOrp() {
    return orp;
  }
  
  @Override
  public String toString() {
    return orp;
  }
}
