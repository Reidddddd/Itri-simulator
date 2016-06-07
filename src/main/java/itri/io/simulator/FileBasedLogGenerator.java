package itri.io.simulator;

import itri.io.simulator.ConditionManager.ConditionIterator;
import itri.io.simulator.para.FileName;
import itri.io.simulator.para.FileRecord;
import itri.io.simulator.util.FileDirectoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileBasedLogGenerator extends LogGenerator<FileName, FileRecord> {
  private final static Log LOG = LogFactory.getLog(FileBasedLogGenerator.class);
  
  private HashMap<FileName, FileRecord> logs;

  public FileBasedLogGenerator(String filePath, IndexInfo info) {
    super(filePath, info);
    logs = new LinkedHashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
  }

  @Override
  public void generate(ConditionManager manager, BufferedReader reader, IndexInfo info) {
    String line = null;
    String groupByName = null;
    String splited[] = null;
    int failCount = 0;
    int failMax = 3;
    int passedCount = 0;
    Conditions cond = null;
    FileName fileName = null;
    FileRecord fileRecord = null;
    int targetPassed = manager.getFiltersNumber();
    ConditionIterator iter = (ConditionIterator) manager.iterator();
    
    LOOP:
    try {
      while ((line = reader.readLine()) != null) {
        /**
         * 1. Filter record
         */
        passedCount = 0;
        splited = trimedArrays(line);
        while (iter.hasNext()) {
          cond = iter.next();
          try {
            if (cond.filter(splited, info)) passedCount++;
            else break;
          } catch (UnsupportedOperationException uoe) {
            // It means that condition is an extraction operation.
            // It is normal and we do it on purpose
            groupByName = ((ExtractConditions) cond).extractGroup(splited, info);
          }
        }
        iter.reset();
        if (passedCount != targetPassed) continue;
        
        /**
         * 2. Put passed record into logs
         */
        fileName = new FileName(groupByName);
        setChanged();
        if (logs.containsKey(fileName)) {
          fileRecord = logs.get(fileName);
          fileRecord.addRecord(splited, info);
          notifyObservers(fileRecord.getRecentlyAddedRecord());
        } else {
          FileRecord newFileRecord = new FileRecord(groupByName);
          newFileRecord.addRecord(splited, info);
          notifyObservers(newFileRecord.getRecentlyAddedRecord());
          logs.put(fileName, newFileRecord);
        }
      }
    } catch (IOException e) {
      LOG.error(e.getMessage());
      LOG.error("Reading " + line + " has problem. Try one more time." );
      if (++failCount < failMax) break LOOP;
    }
  }

  /**
   * Have danger of out of memory.
   * It should be deprecated.
   */
  @Deprecated
  @Override
  public void flush(String outDir) {
    FileDirectoryFactory.makeDir(outDir);
    for (FileRecord fileRecord : logs.values()) {
      String abs = outDir + 
                   File.separator +
                   fileRecord.getGroupName();
      fileRecord.bufferedWriteToFile(abs);
    }
  }

  @Override
  public Map<FileName, FileRecord> getLog() {
    return logs;
  }

  @Override
  public void groupBy(ConditionManager manager) {
    GroupByOption.Option[] groupByOption = { GroupByOption.Option.FILE_NAME };
    manager.addGroupByCondition(groupByOption);
  }
  

  @Override
  public void filterOpr(ConditionManager manager) {
    FilterOption.OprOption[] oprOptions = { FilterOption.OprOption.IRP };
    manager.addFilterOprCondition(oprOptions);
  }

  @Override
  public void filterIrp(ConditionManager manager) {
    FilterOption.IrpOption[] irpOptions = { FilterOption.IrpOption.ALL };
    manager.addFilterIrpCondition(irpOptions);
  }

  @Override
  public void filterMajorOp(ConditionManager manager) {
    FilterOption.MajorOpOption[] majorOpOptions = { FilterOption.MajorOpOption.IRP_READ, 
                                                    FilterOption.MajorOpOption.IRP_WRITE };
    manager.addFilterMajorOpCondition(majorOpOptions);
  }

  @Override
  public void filterStatus(ConditionManager manager) {
    FilterOption.StatusOption[] statusOptions = { FilterOption.StatusOption.SUCCESS };
    manager.addFilterStatusCondition(statusOptions);
  }
}
