package itri.io.simulator.simu;

import itri.io.simulator.para.OperationInfo;
import itri.io.simulator.para.Record;
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
        info.setOpType(lines[Record.OPINFO_TYPE]);
        info.setLength(lines[Record.OPINFO_LENGTH]);
        info.setOffset(lines[Record.OPINFO_OFFSET]);
        info.setIrpFlag(lines[Record.OP_IRPFLAG]);
        op = OperationFactory.getOperation(info);

        fileIndex = FileDirectoryFactory.search(modFiles, FileDirectoryFactory.extractAlpName(lines[Record.OP_FILENAME]));
        if (fileIndex == -1) {
          System.out.println("skip one: " + lines[Record.OP_FILENAME]);
          continue;
        }
        start = System.currentTimeMillis();
        if (!lines[Record.OP_FILENAME].equals(previousFileName)) {
          if (rfile != null) rfile.close();
          previousFileName = lines[Record.OP_FILENAME];
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
