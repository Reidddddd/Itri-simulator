package itri.io.simulator.simu;

import itri.io.simulator.para.FileName;
import itri.io.simulator.para.FileRecord;
import itri.io.simulator.para.OperationInfo;
import itri.io.simulator.para.Record;
import itri.io.simulator.util.FileDirectoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

public class Simulator {
  private String testFile;
  private File directory;

  public Simulator(String testFile) {
    this.testFile = testFile;
  }

  public Simulator(File simuDir, String testFile) {
    this.directory = simuDir;
    this.testFile = testFile;
  }

  public void simulate(Map<FileName, FileRecord> logs) {
    for (Map.Entry<FileName, FileRecord> entry : logs.entrySet()) {
      try (RandomAccessFile file = new RandomAccessFile(testFile, "rw")) {
        Operation op;
        for (Record record : entry.getValue().getRecords()) {
          op = OperationFactory.getOperation(record.getOperationInfo());
          op.operate(file);
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void simulate(String modFileDir) {
    File modDir = new File(modFileDir);
    File[] lists = modDir.listFiles();
    
    File[] files = directory.listFiles();
    Arrays.sort(files, (a, b) -> a.toString().compareTo(b.toString()));
    long start;
    long sumTime = 0;
    int fileIndex;
    for (File file : files) {
      Operation op;
      String line;
      OperationInfo info = new OperationInfo();
      StringTokenizer token;
      
      fileIndex = FileDirectoryFactory.search(lists, file.getName());
      if (fileIndex == -1) {
        System.out.println("skip one: " + lists[fileIndex].getName());
        continue;
      }
      try (RandomAccessFile rfile = new RandomAccessFile(lists[fileIndex], "rw")) {
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

  public void simulate() {
    File[] files = directory.listFiles();
    Arrays.sort(files, (a, b) -> a.toString().compareTo(b.toString()));
    long start;
    long sumTime = 0;
    for (File file : files) {
      Operation op;
      String line;
      OperationInfo info = new OperationInfo();
      StringTokenizer token;
      try (RandomAccessFile rfile = new RandomAccessFile(testFile, "rw")) {
        System.out.println(file.toString());
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
    System.out.println("Simulation time is : " + sumTime);
  }
}
