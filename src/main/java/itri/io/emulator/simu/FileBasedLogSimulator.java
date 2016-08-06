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
import java.util.Arrays;

public class FileBasedLogSimulator extends LogSimulator {
  public FileBasedLogSimulator(String simuDir) {
    super(simuDir);
  }
  
  @Override
  public void simulate(String modFileDir) {
    File modDir = new File(modFileDir);
    File[] modFiles = modDir.listFiles();
    
    File[] simuFiles = simuDir.listFiles();
    Arrays.sort(simuFiles, (a, b) -> a.toString().compareTo(b.toString()));
    long start;
    long sumTime = 0;
    int fileIndex;
    for (File file : simuFiles) {
      Operation op;
      String line;
      OperationInfo info = new OperationInfo();
      String[] token;
      
      fileIndex = FileDirectoryFactory.search(modFiles, file.getName());
      if (fileIndex == -1) {
        System.out.println("skip one, can't find " + file.getName());
        continue;
      }
      try (RandomAccessFile rfile = new RandomAccessFile(modFiles[fileIndex], "rw")) {
        System.out.println(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          while ((line = reader.readLine()) != null) {
            token = line.split(":");
            info.setOpType(token[Record.OPINFO_TYPE]);
            info.setLength(token[Record.OPINFO_LENGTH]);
            info.setOffset(token[Record.OPINFO_OFFSET]);
            info.setIrpFlag(token[Record.OP_IRPFLAG]);
            op = OperationFactory.getOperation(info);
            start = System.currentTimeMillis();
            op.operate(rfile);
            sumTime += (System.currentTimeMillis() - start);
            token = null;
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (IOException ie) {
        ie.printStackTrace();
      }
    }
    System.out.println("Total time is: " + sumTime);
  }
}
