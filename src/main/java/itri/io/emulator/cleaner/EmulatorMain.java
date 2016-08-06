package itri.io.emulator.cleaner;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.Configuration;
import itri.io.emulator.Parameters;
import itri.io.emulator.flusher.FakeFilesFlusher;
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
      addFilters(cleaner, params);
      addFlushers(cleaner, params);
      cleaner.clean();
      LogSimulator simulator = new LogSimulator(params);
      simulator.simulate();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static void addFilters(IOLogCleaner cleaner, Parameters params) {
    cleaner.addFilter(new OperationTypeFilter(params));
    cleaner.addFilter(new MajorOpFilter(params));
    cleaner.addFilter(new StatusFilter(params));
    cleaner.addFilter(new IrpFlagFilter(params));
    cleaner.addFilter(new KeywordFilter(params));
  }

  private static void addFlushers(IOLogCleaner cleaner, Parameters params) {
    cleaner.addObserver(new ReplayLogFlusher(params));
    cleaner.addObserver(new FakeFilesFlusher(params));
  }
}
