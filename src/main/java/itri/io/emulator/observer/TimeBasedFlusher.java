package itri.io.emulator.observer;

import itri.io.emulator.para.FileRecord;
import itri.io.emulator.para.Record;
import itri.io.emulator.util.FileDirectoryFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

public class TimeBasedFlusher extends Flusher {
  private FileRecord wholeRecord;

  public TimeBasedFlusher(String outDir, int recordSize) {
    super(outDir, recordSize);
    wholeRecord = new FileRecord("WholeFile");
  }

  @Override
  public void update(Observable o, Object arg) {
    Record record = (Record) arg;
    wholeRecord.addRecord(record);
    if (++currentSize > bufferSize) {
      flush();
      currentSize = 0;
      wholeRecord.clear();
    }
  }

  @Override
  public void flush() {
    String absPath = outDir + File.separator + "time_sequence_log";
    int writeCount = 0;
    int flushUpperLimit = bufferSize / 3;
    try {
      FileDirectoryFactory.createNewFile(absPath);
      try (FileWriter fw = new FileWriter(new File(absPath), true)) {
        for (Record record : wholeRecord.getRecords()) {
          fw.write(record.toString());
          if (++writeCount >= flushUpperLimit) {
            fw.flush();
            writeCount = 0;
          }
        }
        fw.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
