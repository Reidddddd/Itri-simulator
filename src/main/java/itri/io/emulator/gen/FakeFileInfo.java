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

  public FileName getFileName() {
    return fileName;
  }

  public FileSize getSize() {
    return maxSize;
  }

  class FileSize {
    long size;

    FileSize(String offset, String length) {
      this.size = Long.decode(offset) + Integer.decode(length);
    }

    void updateSize(FileSize size) {
      this.size = size.getSize() > this.size ? size.getSize() : this.size;
    }

    long getSize() {
      return size;
    }
  }
}
