package itri.io.simulator.para;

import itri.io.simulator.FilterOption;

public class Status {
  private final static String SUCCESS = "SUCCESS";
  private final static String WARNING = "WARNING";
  private final static String ERROR = "ERROR";

  private final static String NT_NULL = "0x00000000";
  private final static String NT_SUCCESS = "0x3FFFFFFF";
  private final static String NT_INFORMATION = "0x7FFFFFFF";
  private final static String NT_WARNING = "0xBFFFFFFF";
  private final static String NT_ERROR = "0xFFFFFFFF";

  private boolean isSuccess;
  private boolean isInformation;
  private boolean isWarning;
  private boolean isError;

  public Status(String status) {
    isSuccess     = (status.compareTo(NT_INFORMATION) <= 0 && 
                     status.compareTo(NT_NULL) >= 0) ? true : false;
    isInformation = (status.compareTo(NT_INFORMATION) <= 0 && 
                     status.compareTo(NT_SUCCESS) > 0) ? true : false;
    isWarning     = (status.compareTo(NT_WARNING) <= 0 && 
                     status.compareTo(NT_INFORMATION) > 0) ? true : false;
    isError       = (status.compareTo(NT_ERROR) <= 0 && 
                     status.compareTo(NT_WARNING) > 0) ? true : false;
  }

  public static FilterOption.StatusOption getStatusOption(String statu) {
    FilterOption.StatusOption statuOption;
    switch (statu.toUpperCase()) {
      case SUCCESS: statuOption = FilterOption.StatusOption.SUCCESS; break;
      case WARNING: statuOption = FilterOption.StatusOption.WARNING; break;
      case ERROR: statuOption = FilterOption.StatusOption.ERROR; break;
      default: statuOption = FilterOption.StatusOption.SUCCESS; break;
    }
    return statuOption;
  }

  public static boolean isSuccess(String status) {
    return (status.compareTo(NT_INFORMATION) <= 0 && status.compareTo(NT_NULL) >= 0);
  }

  public static boolean isWarning(String status) {
    return (status.compareTo(NT_WARNING) <= 0 && status.compareTo(NT_INFORMATION) > 0);
  }

  public static boolean isError(String status) {
    return (status.compareTo(NT_ERROR) <= 0 && status.compareTo(NT_WARNING) > 0);
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public boolean isInformation() {
    return isInformation;
  }

  public boolean isWarning() {
    return isWarning;
  }

  public boolean isError() {
    return isError;
  }

  @Override
  public String toString() {
    if (isWarning) return WARNING;
    if (isError) return ERROR;
    return SUCCESS ;
  }
}
