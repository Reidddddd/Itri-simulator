package itri.io.emulator.experiment;

import itri.io.emulator.ConditionManager;
import itri.io.emulator.ConditionManager.ConditionIterator;
import itri.io.emulator.Conditions;
import itri.io.emulator.GroupByOption;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.LogCleaner;
import itri.io.emulator.Parameters;
import itri.io.emulator.gen.FakeFileInfo.FileSize;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.Record;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class ExperimentsManager extends LogCleaner<FileName, FileSize> {

  public ExperimentsManager(String filePath, IndexInfo info) {
    super(filePath, info);
  }

  public void addExperiment(Experiment exp) {
    addObserver(exp);
  }

  public void initialize() throws FileNotFoundException {
    if (reader == null) open();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        setChanged();
        notifyObservers(trimedArrays(line));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      close();
    }
  }

  public void run(Parameters params) {
    this.generate(params);
  }

  public void draw() {
    setChanged();
    notifyObservers();
  }

  @Override
  public Map<FileName, FileSize> getLog() {
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

    LOOP: try {
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
            // do nothing in this time based log generator
          }
        }
        iter.reset();
        if (passedCount != targetPassed) continue;

        /**
         * 2. Put passed record into experiments for draw
         */
        setChanged();
        notifyObservers(new Record(splited, info));
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.err.println("Reading " + line + " has problem. Try one more time.");
      if (++failCount < failMax) break LOOP;
    }
  }

  @Override
  public void groupBy(ConditionManager manager) {
    GroupByOption.Option[] groupByOption = { GroupByOption.Option.TIME_SEQ };
    manager.addGroupByCondition(groupByOption);
  }
}
