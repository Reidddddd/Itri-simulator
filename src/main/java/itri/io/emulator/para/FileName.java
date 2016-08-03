package itri.io.emulator.para;

public class FileName {
  private String unformattedFileName;

  public FileName(String fileName) {
    unformattedFileName = fileName;
  }

  public String getFileName() {
    return unformattedFileName;
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
