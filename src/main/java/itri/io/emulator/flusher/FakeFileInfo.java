package itri.io.emulator.flusher;

import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;

import org.apache.commons.csv.CSVRecord;

/**
 * FakeFileInfo stores the file name and its max size in read or write.
 */
public class FakeFileInfo {
  private FileName fileName;
  private FileSize maxSize;

  public FakeFileInfo(String fileName, long offset, int length) {
    this.fileName = new FileName(fileName);
    this.maxSize = new FileSize(offset, length);
  }

  public FakeFileInfo(CSVRecord record) {
    this.fileName = new FileName(record.get(ColumnConstants.NAME));
    this.maxSize =
        new FileSize(record.get(ColumnConstants.OFFSET), record.get(ColumnConstants.LENGTH));
  }

  public FileName getFileName() {
    return fileName;
  }

  public FileSize getFileSize() {
    return maxSize;
  }
}
