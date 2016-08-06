package itri.io.emulator.parameter;

/**
 * The size of a file. It is used to generate fake files.
 */
public class FileSize {
  private long size;

  public FileSize(String offset, String length) {
    this.size = Long.decode(offset) + Integer.decode(length);
  }

  public FileSize(long offset, int length) {
    this.size = offset + length;
  }

  public void updateSize(FileSize size) {
    this.size = size.getSize() > this.size ? size.getSize() : this.size;
  }

  public long getSize() {
    return size;
  }
}
