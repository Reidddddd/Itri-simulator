package itri.io.emulator.para;

import itri.io.emulator.FilterOption;

/**
 * Operation Type defined by Windows Operating System.
 * Available options are IRP, FIO and FSF.
 */
public class OprFlag {
  private final static String IRP = "IRP";
  private final static String FIO = "FIO";
  private final static String FSF = "FSF";

  private String orp;

  public OprFlag(String opr) {
    switch (opr.toUpperCase()) {
      case IRP: this.orp = IRP; break;
      case FIO: this.orp = FIO; break;
      case FSF: this.orp = FSF; break;
    }
  }

  public static FilterOption.OprOption getOprOption(String opr) {
    FilterOption.OprOption orpOption;
    switch (opr.toUpperCase()) {
      case IRP: orpOption = FilterOption.OprOption.IRP; break;
      case FIO: orpOption = FilterOption.OprOption.FIO; break;
      case FSF: orpOption = FilterOption.OprOption.FSF; break;
        default: orpOption = FilterOption.OprOption.IRP; break;
    }
    return orpOption;
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
