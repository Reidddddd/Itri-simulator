package itri.io.emulator.para;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.util.FileDirectoryFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileRecord {
  private String groupName;
  private LinkedList<Record> records;

  public FileRecord(String groupName) {
    this.groupName = groupName;
    records = new LinkedList<>();
  }

  public void addRecord(String[] splited, IndexInfo info) {
    records.addLast(new Record(splited[info.getOprIndex()], splited[info.getSeqNumIndex()],
                               splited[info.getPreOpTimeIndex()], splited[info.getPostOpTimeIndex()],
                               splited[info.getProcessThrdIndex()], splited[info.getIrpFlagIndex()],
                               splited[info.getStatusIndex()], splited[info.getMajorOpIndex()],
                               splited[info.getLengthIndex()], splited[info.getOffsetIndex()],
                               splited[info.getNameIndex()]));
  }

  public void addRecord(Record record) {
    records.addLast(record);
  }

  public Record getRecentlyAddedRecord() {
    return records.getLast();
  }
  
  public String getGroupName() {
    return groupName;
  }
  
  public List<Record> getRecords() {
    return records;
  }

  public void clear() {
    records.clear();
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
      System.err.println(ie.getMessage());
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
