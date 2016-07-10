package itri.io.simulator.simu;

import itri.io.simulator.para.OperationInfo;
import itri.io.simulator.util.FileDirectoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.lang.StringUtils;

public class TimeBasedLogSimulator extends LogSimulator {

  public TimeBasedLogSimulator(String simuDir) {
    super(simuDir);
  }

  @Override
  public void simulate(String modFileDir) {
    File modDir = new File(modFileDir);
    File[] modFiles = modDir.listFiles();

    File[] simuFiles = simuDir.listFiles();

    long start;
    long sumTime = 0;
    int fileIndex;
    Operation op;
    String line;
    OperationInfo info = new OperationInfo();
    String previousFileName = "";
    RandomAccessFile rfile = null;

    try (BufferedReader reader = new BufferedReader(new FileReader(simuFiles[0]))) {
      while ((line = reader.readLine()) != null) {
        String[] lines = StringUtils.split(line, ":");
        info.setOpType(lines[0]);
        info.setLength(lines[1]);
        info.setOffset(lines[2]);
        op = OperationFactory.getOperation(info);

        fileIndex = FileDirectoryFactory.search(modFiles, FileDirectoryFactory.extractAlpName(lines[lines.length - 1]));
        if (fileIndex == -1) {
          System.out.println("skip one: " + lines[lines.length - 1]);
          continue;
        }
        start = System.currentTimeMillis();
        if (!lines[lines.length - 1].equals(previousFileName)) {
          if (rfile != null) rfile.close();
          previousFileName = lines[lines.length - 1];
          rfile = new RandomAccessFile(modFiles[fileIndex], "rw");
        }
        op.operate(rfile);
        sumTime += (System.currentTimeMillis() - start);
        lines = null;
      }
    } catch (IOException ie) {
      ie.printStackTrace();
    } finally {
      try {
        if (rfile != null) rfile.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Total time is: " + sumTime);
  }
}
