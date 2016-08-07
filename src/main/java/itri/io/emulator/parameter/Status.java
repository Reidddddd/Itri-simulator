package itri.io.emulator.parameter;

import itri.io.emulator.cleaner.FilterOption;

/**
 * Status of an operation. Available status are success, warning, error.
 */
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
    if (!isParsable(status)) return;
    isSuccess =
        compare(status, NT_INFORMATION) <= 0 && compare(status, NT_NULL) >= 0 ? true : false;
    isInformation =
        compare(status, NT_INFORMATION) <= 0 && compare(status, NT_SUCCESS) > 0 ? true : false;
    isWarning =
        compare(status, NT_WARNING) <= 0 && compare(status, NT_INFORMATION) > 0 ? true : false;
    isError = compare(status, NT_ERROR) <= 0 && compare(status, NT_WARNING) > 0 ? true : false;
  }

  public static FilterOption.StatusOption getStatusOption(String statu) {
    FilterOption.StatusOption statuOption;
    switch (statu.toUpperCase()) {
      case SUCCESS:
        statuOption = FilterOption.StatusOption.SUCCESS;
        break;
      case WARNING:
        statuOption = FilterOption.StatusOption.WARNING;
        break;
      case ERROR:
        statuOption = FilterOption.StatusOption.ERROR;
        break;
      default:
        statuOption = FilterOption.StatusOption.NONE;
        break;
    }
    return statuOption;
  }

  public static boolean isSuccess(String status) {
    if (!isParsable(status)) return false;
    return compare(status, NT_INFORMATION) <= 0 && compare(status, NT_NULL) >= 0;
  }

  public static boolean isWarning(String status) {
    if (!isParsable(status)) return false;
    return compare(status, NT_WARNING) <= 0 && compare(status, NT_INFORMATION) > 0;
  }

  public static boolean isError(String status) {
    if (!isParsable(status)) return false;
    return compare(status, NT_ERROR) <= 0 && compare(status, NT_WARNING) > 0;
  }

  private static boolean isParsable(String status) {
    return status.startsWith("0x") || status.startsWith("0X");
  }
  private static int compare(String left, String right) {
    return Long.compare(Long.decode(left), Long.decode(right));
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
    return SUCCESS;
  }
}
