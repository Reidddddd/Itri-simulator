package itri.io.emulator.flusher;

import itri.io.emulator.Parameters;
import itri.io.emulator.cleaner.IOLogCleaner.Tuple;
import itri.io.emulator.para.FileRecord;
import itri.io.emulator.para.Record;
import itri.io.emulator.util.FileDirectoryFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * ReplayLogFlusher is used to generate replay log.
 */
public class ReplayLogFlusher extends Flusher implements Observer {
  private FileRecord wholeRecords;
  private String replayLogDir;
  private int bufferSize;
  private int currentSize;

  public ReplayLogFlusher(Parameters params) {
    this.wholeRecords = new FileRecord("WholeFile");
    this.replayLogDir = params.getReplayLogOutputLocation();
    this.bufferSize = params.getBufferSize();
    this.currentSize = 0;
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg == null) flush();
    else if (arg.getClass() == Tuple.class) {
      Tuple tuple = (Tuple) arg;
      if (tuple.getFlusherType() == FlusherType.REPLAY_LOG) {
        Record record = new Record(tuple.getRecord());
        wholeRecords.addRecord(record);
        if (++currentSize > bufferSize) {
          flush();
          currentSize = 0;
          wholeRecords.clear();
        }
      }
    }
  }

  public void flush() {
    String absPath = replayLogDir + File.separator + "time_sequence_log";
    int writeCount = 0;
    int flushUpperLimit = bufferSize / 3;
    try {
      FileDirectoryFactory.createNewFile(absPath);
      try (FileWriter fw = new FileWriter(new File(absPath), true)) {
        for (Record record : wholeRecords.getRecords()) {
          fw.write(record.toString());
          if (++writeCount >= flushUpperLimit) {
            fw.flush();
            writeCount = 0;
          }
        }
        fw.flush();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
