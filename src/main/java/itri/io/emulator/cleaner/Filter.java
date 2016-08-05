package itri.io.emulator.cleaner;

import org.apache.commons.csv.CSVRecord;

public abstract class Filter {
  public abstract boolean filter(CSVRecord record);
}
