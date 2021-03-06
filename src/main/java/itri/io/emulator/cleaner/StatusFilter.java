package itri.io.emulator.cleaner;

import itri.io.emulator.cleaner.FilterOption.StatusOption;
import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.parameter.Status;

import org.apache.commons.csv.CSVRecord;

/**
 * Status Filter
 */
public class StatusFilter extends Filter {
  private StatusOption[] statusFilters;

  public StatusFilter(Parameters params) {
    this.statusFilters = new StatusOption[params.getStatusNames().length];
    int index = 0;
    for (String sta : params.getStatusNames()) {
      statusFilters[index++] = Status.getStatusOption(sta);
    }
  }

  @Override
  public boolean filter(CSVRecord record) {
    for (int i = 0; i < statusFilters.length; i++) {
      switch (statusFilters[i]) {
        case SUCCESS: {
          if (Status.isSuccess(record.get(ColumnConstants.STATUS))) return true;
          break;
        }
        case WARNING: {
          if (Status.isWarning(record.get(ColumnConstants.STATUS))) return true;
          break;
        }
        case ERROR: {
          if (Status.isError(record.get(ColumnConstants.STATUS))) return true;
          break;
        }
        default: return false;
      }
    }
    return false;
  }

  @Override
  public void setFilterOptions(Object options) {
    if (options.getClass() != StatusOption[].class) return;
    this.statusFilters = (StatusOption[]) options;
  }
}
