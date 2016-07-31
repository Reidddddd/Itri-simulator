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
