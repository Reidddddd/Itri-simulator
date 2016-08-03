package itri.io.emulator;

import itri.io.emulator.ConditionManager.ConditionIterator;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.FileRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileBasedLogCleaner extends LogCleaner<FileName, FileRecord> {

  private HashMap<FileName, FileRecord> logs;

  public FileBasedLogCleaner(String filePath, IndexInfo info) {
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
    System.out.println(targetPassed);
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
        setChanged();
        notifyObservers(splited);
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
      System.err.println(e.getMessage());
      System.err.println("Reading " + line + " has problem. Try one more time." );
      if (++failCount < failMax) break LOOP;
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
}
