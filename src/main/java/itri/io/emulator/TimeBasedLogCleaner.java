package itri.io.emulator;

import itri.io.emulator.ConditionManager.ConditionIterator;
import itri.io.emulator.gen.FakeFileInfo;
import itri.io.emulator.para.FileRecord;
import itri.io.emulator.para.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class TimeBasedLogCleaner extends LogCleaner<Integer, FileRecord> {

  public TimeBasedLogCleaner(String filePath, IndexInfo info) {
    super(filePath, info);
  }

  @Override
  public Map<Integer, FileRecord> getLog() {
    return null;
  }

  @Override
  public void generate(ConditionManager manager, BufferedReader reader, IndexInfo info) {
    String line = null;
    String splited[] = null;
    int failCount = 0;
    int failMax = 3;
    int passedCount = 0;
    Conditions cond = null;
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
        setChanged();
        notifyObservers(new FakeFileInfo(splited, info));
        while (iter.hasNext()) {
          cond = iter.next();
          try {
            if (cond.filter(splited, info)) passedCount++;
            else break;
          } catch (UnsupportedOperationException uoe) {
            // do nothing in this time based log generator
          }
        }
        iter.reset();
        if (passedCount != targetPassed) continue;
        
        /**
         * 2. Put passed record into appender for flush
         */
        setChanged();
        notifyObservers(new Record(splited, info));
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.err.println("Reading " + line + " has problem. Try one more time." );
      if (++failCount < failMax) break LOOP;
    }
  }

  @Override
  public void groupBy(ConditionManager manager) {
    GroupByOption.Option[] groupByOption = { GroupByOption.Option.TIME_SEQ };
    manager.addGroupByCondition(groupByOption);
  }
}
