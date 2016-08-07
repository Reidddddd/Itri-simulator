package itri.io.emulator.flusher;

import itri.io.emulator.Parameters;
import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.gen.FakeFileInfo;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;
import itri.io.emulator.util.FileDirectoryFactory;
import itri.io.emulator.util.RandomTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.csv.CSVRecord;

/**
 * FakeFilesFlusher is used to generate files for simulator read/write.
 */
public class FakeFilesFlusher extends Flusher {
  private static int INITIAL_CAPACITY = 200;
  private static float LOAD_FACTOR = 0.75f;

  private Map<FileName, FileSize> fileMaxSize;
  private String fakeFilesDir;

  public FakeFilesFlusher(Parameters params) {
    super();
    this.fakeFilesDir = params.getFakeFilesLocation();
    this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg == null) {
      flush();
      return;
    }
    CSVRecord record = (CSVRecord) arg;
    for (Filter filter : filters) {
      if (!filter.filter(record)) return;
    }
    FakeFileInfo fake = new FakeFileInfo(record);
    if (fileMaxSize.get(fake.getFileName()) == null) {
      fileMaxSize.put(fake.getFileName(), fake.getFileSize());
    } else {
      fileMaxSize.get(fake.getFileName()).updateSize(fake.getFileSize());
    }
  }

  @Override
  public void flush() {
    System.out.println("Start generate fake files.");
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
