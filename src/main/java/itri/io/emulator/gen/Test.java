package itri.io.emulator.gen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.observer.Flusher;
import itri.io.emulator.para.FileName;
import itri.io.emulator.util.FileDirectoryFactory;
import itri.io.emulator.util.RandomTools;

public abstract class Test extends Flusher {

  public Test(String outDir, int bufferSize) {
    super(outDir, bufferSize);
  }

  @Override
  public void flush() {
    ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      String absPath =
          outDir + File.separator
              + FileDirectoryFactory.extractAlpName(entry.getKey().getFileName());
      long fileSize = entry.getValue().getSize();
      try {
        FileDirectoryFactory.createNewFile(absPath);
        try (FileWriter fw = new FileWriter(new File(absPath), true)) {
          for (long writtenSize = 0; writtenSize <= fileSize; writtenSize += 65536) {
            if ((writtenSize + 65536) <= fileSize) fw.write(new String(RandomTools.generateByte(65536)));
            else fw.write(new String(RandomTools.generateByte((int) (fileSize - writtenSize))));
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