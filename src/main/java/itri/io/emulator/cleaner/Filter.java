package itri.io.emulator.cleaner;

import org.apache.commons.csv.CSVRecord;

/**
 * Abstract class of Filter
 */
public abstract class Filter {
  public abstract boolean filter(CSVRecord record);

  public abstract void setFilterOptions(Object options);
}
