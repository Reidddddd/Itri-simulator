package itri.io.emulator.gen;

import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.observer.Flusher;
import itri.io.emulator.para.FileName;
import itri.io.emulator.util.FileDirectoryFactory;
import itri.io.emulator.util.RandomTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileGenerator extends Flusher {
  private Map<FileName, FileSize> fileMaxSize;
  private static int INITIAL_CAPACITY = 200;
  private static float LOAD_FACTOR = 0.75f;

  public FileGenerator(String outDir, int bufferSize) {
    super(outDir, bufferSize);
    fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg.getClass() != FakeFileInfo.class) return;
    FakeFileInfo fake = (FakeFileInfo) arg;
    if (fileMaxSize.get(fake.getFileName()) == null) {
      fileMaxSize.put(fake.getFileName(), fake.getSize());
    } else {
      fileMaxSize.get(fake.getFileName()).updateSize(fake.getSize());
    }
  }

  @Override
  public void flush() {
    ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      service.execute(new FastGeneration(entry.getKey().getFileName(), entry.getValue().getSize()));
    }
    service.shutdown();
    try {
      while (true) {
        if (service.isTerminated()) {
          break;
        }
        Thread.sleep(1000 * 10);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private class FastGeneration implements Runnable {
    String absPath;
    long fileSize;

    public FastGeneration(String fileName, long fileSize) {
      this.absPath = outDir + File.separator + FileDirectoryFactory.extractAlpName(fileName);
      this.fileSize = fileSize; 
    }

    @Override
    public void run() {
      try {
        FileDirectoryFactory.createNewFile(absPath);
        try (FileWriter fw = new FileWriter(new File(absPath), true)) {
          for (long writtenSize = 0; writtenSize <= fileSize; writtenSize += 65536) {
            if ((writtenSize + 65536) <= fileSize) {
              fw.write(new String(RandomTools.generateByte(65536)));
            } else {
              fw.write(new String(RandomTools.generateByte((int) (fileSize - writtenSize))));
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
