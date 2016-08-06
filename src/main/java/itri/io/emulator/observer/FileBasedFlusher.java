package itri.io.emulator.observer;

import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.Record;
import itri.io.emulator.util.FileDirectoryFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class FileBasedFlusher extends Flusher {
  private Map<FileName, List<Record>> buffer;
  
  public FileBasedFlusher(String outDir, int bufferSize) {
    super(outDir, bufferSize);
    FileDirectoryFactory.makeDir(outDir);
    buffer = new HashMap<>();
    currentSize = 0;
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg.getClass() != Record.class) return;
    Record record = (Record) arg;
    if (buffer.containsKey(record.getFName())) {
      buffer.get(record.getFName()).add(record);
    } else {
      List<Record> records = new LinkedList<>();
      records.add(record);
      buffer.put(record.getFName(), records);
    }
    if (++currentSize >= bufferSize) {
      flush();
      currentSize = 0;
      buffer.clear();
    }
  }

  @Override
  public void flush() {
    String absPath;
    for (Map.Entry<FileName, List<Record>> entry : buffer.entrySet()) {
      absPath = outDir + File.separator +
                FileDirectoryFactory.extractAlpName(entry.getKey().getFileName());
      try {
        FileDirectoryFactory.createNewFile(absPath);
        try (FileWriter fw = new FileWriter(new File(absPath), true)) {
          for (Record record : entry.getValue()) {
            fw.write(record.toString());
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
