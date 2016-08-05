package itri.io.emulator.cleaner;

import org.apache.commons.csv.CSVRecord;

/**
 * Default filter, all pass.
 */
public class DefaultFilter extends Filter {

  @Override
  public boolean filter(CSVRecord record) {
    return true;
  }

}
