package itri.io.emulator.gen;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.observer.Flusher;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.MajorOp;
import itri.io.emulator.util.FileDirectoryFactory;
import itri.io.emulator.util.RandomTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class FileGenerator extends Flusher {
  private static int INITIAL_CAPACITY = 200;
  private static float LOAD_FACTOR = 0.75f;

  private Map<FileName, FileSize> fileMaxSize;
  private IndexInfo info;

  public FileGenerator(String outDir, IndexInfo info) {
    this(outDir, 0, info);
  }

  public FileGenerator(String outDir, int bufferSize, IndexInfo info) {
    super(outDir, bufferSize);
    fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
    this.info = info;
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg.getClass() != String[].class) return;
    String[] splited = (String[]) arg;
    if (MajorOp.isWrite(splited[info.getMajorOpIndex()])) return;
    FakeFileInfo fake = new FakeFileInfo(splited, info);
    if (fileMaxSize.get(fake.getFileName()) == null) {
      fileMaxSize.put(fake.getFileName(), fake.getFileSize());
    } else {
      fileMaxSize.get(fake.getFileName()).updateSize(fake.getFileSize());
    }
  }

  @Override
  public void flush() {
    for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
      String absPath =
          outDir + File.separator
              + FileDirectoryFactory.extractAlpNameWithoutNumPrefix(entry.getKey().getFileName());
      long fileSize = entry.getValue().getSize();
      long UNIT = 1024 * 1024;
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
