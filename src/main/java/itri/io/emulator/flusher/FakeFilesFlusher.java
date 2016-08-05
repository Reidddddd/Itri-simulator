package itri.io.emulator.flusher;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.Parameters;
import itri.io.emulator.cleaner.IOLogCleaner.Tuple;
import itri.io.emulator.gen.FakeFileInfo;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.FileSize;
import itri.io.emulator.para.MajorOp;
import itri.io.emulator.util.FileDirectoryFactory;
import itri.io.emulator.util.RandomTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.csv.CSVRecord;

/**
 * FakeFilesFlusher is used to generate files for simulator read/write.
 */
public class FakeFilesFlusher extends Flusher implements Observer {
  private static int INITIAL_CAPACITY = 200;
  private static float LOAD_FACTOR = 0.75f;

  private Map<FileName, FileSize> fileMaxSize;
  private String fakeFilesDir;

  public FakeFilesFlusher(Parameters params) {
    this.fakeFilesDir = params.getFakeFilesLocation();
    this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg.getClass() == null) flush();
    else if (arg.getClass() == Tuple.class) {
      Tuple tuple = (Tuple) arg;
      if (tuple.getFlusherType() == FlusherType.FAKE_FILE) {
        CSVRecord record = tuple.getRecord();
        // We only care read and write.
        if (!MajorOp.isWrite(record.get(ColumnConstants.MAJOR_OP))
            && !MajorOp.isRead(record.get(ColumnConstants.MAJOR_OP))) return;
        FakeFileInfo fake = new FakeFileInfo(record);
        if (fileMaxSize.get(fake.getFileName()) == null) {
          fileMaxSize.put(fake.getFileName(), fake.getFileSize());
        } else {
          fileMaxSize.get(fake.getFileName()).updateSize(fake.getFileSize());
        }
      }
    }
  }

  @Override
  public void flush() {
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      String absPath =
          fakeFilesDir + File.separator
              + FileDirectoryFactory.extractNameOnlyLettersAndDigits(entry.getKey().getFileName());
      long fileSize = entry.getValue().getSize();
      long UNIT = 1024 * 1024; // 1M
      try {
        FileDirectoryFactory.createNewFile(absPath);
        try (FileOutputStream fw = new FileOutputStream(new File(absPath), true)) {
          for (long writtenSize = 0; writtenSize <= fileSize; writtenSize += UNIT) {
            if ((writtenSize + UNIT) <= fileSize) {
              fw.write(RandomTools.generateByte((int) UNIT));
            } else {
              fw.write(RandomTools.generateByte((int) (fileSize - writtenSize)));
            }
          }
          fw.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }
}
