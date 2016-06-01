package itri.io.simulator.para;

import itri.io.simulator.IndexInfo;
import itri.io.simulator.util.FileDirectoryFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileRecord {
  private final static Log LOG = LogFactory.getLog(FileRecord.class);
  
  private String groupName;
  private List<Record> records;

  public FileRecord(String groupName) {
    this.groupName = groupName;
    records = new ArrayList<>();
  }

  public void addRecord(String[] splited, IndexInfo info) {
    records.add(new Record(splited[info.getOprIndex()], splited[info.getSeqNumIndex()],
                           splited[info.getPreOpTimeIndex()], splited[info.getPostOpTimeIndex()],
                           splited[info.getProcessThrdIndex()], splited[info.getIrpFlagIndex()],
                           splited[info.getStatusIndex()], splited[info.getMajorOpIndex()],
                           splited[info.getLengthIndex()], splited[info.getOffsetIndex()],
                           splited[info.getNameIndex()]));
  }
  
  public Record getRecentlyAddedRecord() {
    return records.get(records.size() - 1);
  }
  
  public String getGroupName() {
    return groupName;
  }
  
  public List<Record> getRecords() {
    return records;
  }
  
  @Deprecated
  public void writeToFile(String outPath) {
    try (PrintWriter writer = new PrintWriter(FileDirectoryFactory.createNewFile(outPath))) {
      writer.write(this.toString());
    } catch (IOException e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
  }
  
  public void bufferedWriteToFile(String outPath) {
    try (BufferedWriter writer =
         new BufferedWriter(new FileWriter(FileDirectoryFactory.createNewFile(outPath)))) {
      writer.write("File: " + groupName + "\n");
      for (Record entry : records) {
        writer.write(entry + "\n");
      }
      writer.flush();
    } catch (IOException ie) {
      ie.printStackTrace();
      LOG.error(ie.getMessage());
    }
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(groupName);
    builder.append("\n[\n");
    for (Record entry : records) {
      builder.append("\t" + entry + "\n");
    }
    builder.append("]\n");
    return builder.toString();
  }
}
