package itri.io.emulator.cleaner;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.Parameters;

import org.apache.commons.csv.CSVRecord;

/**
 * Keyword Filter.
 * Keyword is the name column in the original sheet.
 */
public class KeywordFilter extends Filter {
  private String[] keywords;

  public KeywordFilter(Parameters params) {
    this.keywords = params.getKeyWordNames();
  }

  @Override
  public boolean filter(CSVRecord record) {
    for (String name : keywords) {
      if (record.get(ColumnConstants.NAME).contains(name)) return true;
    }
    return false;
  }
}
