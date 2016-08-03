package itri.io.emulator.gen;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.para.FileName;

public class FakeFileInfo {
  private FileName fileName;
  private FileSize maxSize;

  public FakeFileInfo(String[] splited, IndexInfo info) {
    this.fileName = new FileName(splited[info.getNameIndex()]);
    this.maxSize = new FileSize(splited[info.getOffsetIndex()], splited[info.getLengthIndex()]);
  }

  public FakeFileInfo(String fileName, long offset, int length) {
    this.fileName = new FileName(fileName);
    this.maxSize = new FileSize(offset, length);
  }

  public FileName getFileName() {
    return fileName;
  }

  public FileSize getFileSize() {
    return maxSize;
  }

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
}
