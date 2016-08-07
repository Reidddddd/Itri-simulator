package itri.io.emulator.cleaner;

import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Parameters;

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

  @Override
  public void setFilterOptions(Object options) {
    if (options.getClass() != String[].class) return;
    this.keywords = (String[]) options;
  }
}
