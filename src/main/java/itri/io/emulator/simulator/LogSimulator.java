package itri.io.emulator.simulator;

import itri.io.emulator.Parameters;
import itri.io.emulator.para.OperationInfo;
import itri.io.emulator.para.Record;
import itri.io.emulator.util.FileDirectoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogSimulator {
  private File replayLogLocation;
  private File fakeFileLocation;

  public LogSimulator(Parameters params) {
    this.replayLogLocation = new File(params.getReplayLogOutputLocation());
    this.fakeFileLocation = new File(params.getFakeFilesLocation());
  }

  public void simulate() {
    File[] replayLogs = replayLogLocation.listFiles();
    File[] fakeFiles = fakeFileLocation.listFiles();

    long start;
    long sumTime = 0;
    int fileIndex;
    Operation op;
    String line;
    OperationInfo info = new OperationInfo();
    String previousFileName = "";
    RandomAccessFile rfile = null;

    try (BufferedReader reader = new BufferedReader(new FileReader(replayLogs[0]))) {
      while ((line = reader.readLine()) != null) {
        String[] lines = line.split(":");
        info.setOpType(lines[Record.OPINFO_TYPE]);
        info.setLength(lines[Record.OPINFO_LENGTH]);
        info.setOffset(lines[Record.OPINFO_OFFSET]);
        info.setIrpFlag(lines[Record.OP_IRPFLAG]);
        op = OperationFactory.getOperation(info);

        fileIndex =
            FileDirectoryFactory.search(fakeFiles,
              FileDirectoryFactory.extractNameOnlyLettersAndDigits(lines[Record.OP_FILENAME]));
        if (fileIndex == -1) {
          System.out.println("skip one, can't find " + lines[Record.OP_FILENAME]);
          continue;
        }
        start = System.currentTimeMillis();
        if (!lines[Record.OP_FILENAME].equals(previousFileName)) {
          if (rfile != null) rfile.close();
          previousFileName = lines[Record.OP_FILENAME];
          rfile = new RandomAccessFile(fakeFiles[fileIndex], "rw");
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
