package itri.io.emulator.simu;

import itri.io.emulator.para.OperationInfo;
import itri.io.emulator.para.Record;
import itri.io.emulator.simulator.Operation;
import itri.io.emulator.simulator.OperationFactory;
import itri.io.emulator.util.FileDirectoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

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
        String[] lines = line.split(":");
        info.setOpType(lines[Record.OPINFO_TYPE]);
        info.setLength(lines[Record.OPINFO_LENGTH]);
        info.setOffset(lines[Record.OPINFO_OFFSET]);
        info.setIrpFlag(lines[Record.OP_IRPFLAG]);
        op = OperationFactory.getOperation(info);

        fileIndex =
            FileDirectoryFactory.search(modFiles,
              FileDirectoryFactory.extractAlpName(lines[Record.OP_FILENAME]));
        if (fileIndex == -1) {
          System.out.println("skip one, can't find " + lines[Record.OP_FILENAME]);
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
  
  public static void main(String[] args) {
    File file = new File("/home/reidchan/Simulog_1");
    System.out.println(file.getAbsolutePath());
  }
}
