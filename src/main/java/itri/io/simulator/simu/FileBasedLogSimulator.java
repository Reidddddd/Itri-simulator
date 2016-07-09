package itri.io.simulator.simu;

import itri.io.simulator.para.OperationInfo;
import itri.io.simulator.util.FileDirectoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.StringTokenizer;

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
      StringTokenizer token;
      
      fileIndex = FileDirectoryFactory.search(modFiles, file.getName());
      if (fileIndex == -1) {
        System.out.println("skip one: " + modFiles[fileIndex].getName());
        continue;
      }
      try (RandomAccessFile rfile = new RandomAccessFile(modFiles[fileIndex], "rw")) {
        System.out.println(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          while ((line = reader.readLine()) != null) {
            token = new StringTokenizer(line, ":");
            info.setOpType(token.nextToken());
            info.setLength(token.nextToken());
            info.setOffset(token.nextToken());
            op = OperationFactory.getOperation(info);
            start = System.currentTimeMillis();
            op.operate(rfile);
            sumTime += (System.currentTimeMillis() - start);
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
