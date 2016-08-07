package itri.io.emulator.cleaner;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.Configuration;
import itri.io.emulator.Parameters;
import itri.io.emulator.cleaner.FilterOption.MajorOpOption;
import itri.io.emulator.flusher.FakeFilesFlusher;
import itri.io.emulator.flusher.FilterLogFlusher;
import itri.io.emulator.flusher.ReplayLogFlusher;
import itri.io.emulator.simulator.LogSimulator;

import java.io.IOException;

/**
 * EmulatorMain will run all processes, including i/o log clean, replay log flush, fake file
 * generate and i/o simulate.
 */
public class EmulatorMain {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Please input the location of configuration file.");
      System.exit(0);
    }
    Configuration conf = new Configuration(args[0]);
    Parameters params = new Parameters(conf);
    try (IOLogCleaner cleaner = new IOLogCleaner(params, ColumnConstants.getColumnsHeader())) {
      addFlushers(cleaner, params);
      System.out.println("Start generate replay log.");
      cleaner.clean();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    LogSimulator simulator = new LogSimulator(params);
    simulator.simulate();
    System.out.println("Emulator is done.");
  }

  private static void addFlushers(IOLogCleaner cleaner, Parameters params) {
    ReplayLogFlusher replayLogFlusher = new ReplayLogFlusher(params);
    replayLogFlusher.addFilter(new OperationTypeFilter(params));
    replayLogFlusher.addFilter(new MajorOpFilter(params));
    replayLogFlusher.addFilter(new StatusFilter(params));
    replayLogFlusher.addFilter(new IrpFlagFilter(params));
    replayLogFlusher.addFilter(new KeywordFilter(params));
    cleaner.addFlusher(replayLogFlusher);

    MajorOpFilter majorOpFilter = new MajorOpFilter(params);
    MajorOpOption[] options = { MajorOpOption.IRP_READ, MajorOpOption.IRP_WRITE };
    majorOpFilter.setFilterOptions(options);
    FakeFilesFlusher fakeFilesFlusher = new FakeFilesFlusher(params);
    fakeFilesFlusher.addFilter(majorOpFilter);
    fakeFilesFlusher.addFilter(new KeywordFilter(params));
    cleaner.addFlusher(fakeFilesFlusher);
    
    FilterLogFlusher filterLogFlusher = new FilterLogFlusher(params);
    filterLogFlusher.addFilter(new ProcessFilter(params));
    cleaner.addFlusher(filterLogFlusher);
  }
}
