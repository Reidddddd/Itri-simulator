package itri.io.emulator.experiment;

import itri.io.emulator.ConditionManager;
import itri.io.emulator.ConditionManager.ConditionIterator;
import itri.io.emulator.Conditions;
import itri.io.emulator.GroupByOption;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.LogCleaner;
import itri.io.emulator.Parameters;
import itri.io.emulator.para.FileName;
import itri.io.emulator.para.FileSize;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class ExperimentsManager extends LogCleaner<FileName, FileSize> {
  private Visitor visitor;

  public ExperimentsManager(String filePath, IndexInfo info) {
    super(filePath, info);
  }

  public void addExperiment(Experiment exp) {
    addObserver(exp);
  }

  public void initialize(Parameters params) throws FileNotFoundException {
    visitor = new Visitor(ExperimentState.PREPROCESS) {
      @Override
      Tuple visit(String[] splited) {
        return new Tuple(splited, this.state);
      }
    };
    this.generate(params);
  }

  public void run() {
    visitor = new Visitor(ExperimentState.PROCESS) {
      @Override
      Tuple visit(String[] splited) {
        return new Tuple(splited, this.state);
      }
    };

    try {
      open();
      this.generate(manager, reader, info);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      close();
    }
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
        notifyObservers(visitor.visit(splited));
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

  abstract class Visitor {
    ExperimentState state;

    public Visitor(ExperimentState state) {
      this.state = state;
    }

    abstract Tuple visit(String[] splited);
  }

  class Tuple {
    String[] splited;
    ExperimentState state;

    Tuple(String[] splited, ExperimentState state) {
      this.splited = splited;
      this.state = state;
    }

    String[] getSplited() {
      return splited;
    }

    ExperimentState getState() {
      return state;
    }
  }

  enum ExperimentState {
    PREPROCESS, PROCESS
  }
}
