package itri.io.simulator.observer;

import itri.io.simulator.para.Record;
import itri.io.simulator.util.FileDirectoryFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class FileAppender implements Observer {
  private String parentPath;
  
  public FileAppender(String outDir) {
    parentPath = outDir;
    FileDirectoryFactory.makeDir(parentPath);
  }
  
  @Override
  public void update(Observable o, Object arg) {
    Record record = (Record) arg;
    String absPath = parentPath + File.separator + 
                     FileDirectoryFactory.extractAlpName(record.getFName().getFileName());
    try {
      FileDirectoryFactory.createNewFile(absPath);
      try (FileWriter fw = new FileWriter(new File(absPath), true)) {
        fw.write(record.toString());
        fw.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
