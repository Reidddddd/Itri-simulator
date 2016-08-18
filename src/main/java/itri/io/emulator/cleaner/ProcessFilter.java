package itri.io.emulator.cleaner;

import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Parameters;
import org.apache.commons.csv.CSVRecord;

/**
 * Process Filter
 */
public class ProcessFilter extends Filter {
	private String[] processNames;

	public ProcessFilter(Parameters params) {
	  this.processNames = params.getProcessNames();
	}

	@Override
	public boolean filter(CSVRecord record) {
	  for (String name : processNames) {
	    if (record.get(ColumnConstants.PROCESS_THRD).contains(name+'.')) return true;
	  }
	  return false;
	}

	@Override
	public void setFilterOptions(Object options) {
	  if (options.getClass() != String[].class) return;
	  this.processNames = (String[]) options;
	}
}
