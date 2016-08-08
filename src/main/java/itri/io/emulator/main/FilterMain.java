package itri.io.emulator.main;

import java.io.IOException;

import itri.io.emulator.cleaner.IOLogCleaner;
import itri.io.emulator.cleaner.IrpFlagFilter;
import itri.io.emulator.cleaner.KeywordFilter;
import itri.io.emulator.cleaner.MajorOpFilter;
import itri.io.emulator.cleaner.OperationTypeFilter;
import itri.io.emulator.cleaner.ProcessFilter;
import itri.io.emulator.cleaner.StatusFilter;
import itri.io.emulator.cleaner.FilterOption.MajorOpOption;
import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Configuration;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.flusher.FakeFilesFlusher;
import itri.io.emulator.flusher.FilterLogFlusher;
import itri.io.emulator.flusher.ReplayLogFlusher;
import itri.io.emulator.simulator.LogSimulator;

/**
 * FilterMain will filter the log that contains those process-id which we want.
 */
public class FilterMain {
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println("Please input the location of configuration file.");
			System.exit(0);
		}
		Configuration conf = new Configuration(args[0]);
		Parameters params = new Parameters(conf);
		try (IOLogCleaner cleaner = new IOLogCleaner(params, ColumnConstants.getColumnsHeader())) {
			addFlushers(cleaner, params);
			System.out.println("Start filter log.");
			cleaner.clean();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Emulator is done.");
	}

	private static void addFlushers(IOLogCleaner cleaner, Parameters params) {
		FilterLogFlusher filterLogFlusher = new FilterLogFlusher(params);
		filterLogFlusher.addFilter(new ProcessFilter(params));
		cleaner.addFlusher(filterLogFlusher);
	}
}
