package itri.io.emulator.para;

public class FileName {
  private String unformattedFileName;
  private String deviceName;
  private String hardDiskName;

  public FileName(String fileName) {
    unformattedFileName = fileName;
    // try {
    // String[] names = StringUtils.split(fileName, "\\");
    // deviceName = getDeviceName(names);
    // hardDiskName = getHardDiskName(names);
    // formattedFileName = getRemainingName(names);
    // } catch (IndexOutOfBoundsException idbe) {
    // deviceName = "attach no device";
    // hardDiskName = "attach no hard disk";
    // formattedFileName = unformattedFileName;
    // }
  }

  public String getFileName() {
    return unformattedFileName;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public String getHardDiskName() {
    return hardDiskName;
  }

  private static String getDeviceName(String[] names) {
    return names[1];
  }

  private static String getHardDiskName(String[] names) {
    return names[2];
  }

  private static String getRemainingName(String[] names) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < names.length; i++) {
      if (i == 0 || i == 1 || i == 2) continue;
      if (i == names.length - 1) builder.append(names[i]);
      else builder.append(names[i] + "-");
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof FileName) {
      FileName fn = (FileName) obj;
      return fn.unformattedFileName.equals(this.unformattedFileName);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return unformattedFileName.hashCode();
  }

  @Override
  public String toString() {
    return unformattedFileName;
  }
}
