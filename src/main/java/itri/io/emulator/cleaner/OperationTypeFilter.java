package itri.io.emulator.cleaner;

import itri.io.emulator.cleaner.FilterOption.OprOption;
import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.parameter.OprFlag;

import org.apache.commons.csv.CSVRecord;

/**
 * Operation Type Filter
 */
public class OperationTypeFilter extends Filter {
  private OprOption[] oprOption;

  public OperationTypeFilter(Parameters params) {
    oprOption = new OprOption[params.getOprNames().length];
    int index = 0;
    for (String opr : params.getOprNames()) {
      oprOption[index++] = OprFlag.getOprOption(opr);
    }
  }

  @Override
  public boolean filter(CSVRecord record) {
    for (OprOption opr : oprOption) {
      switch (opr) {
        case IRP: {
          if (OprFlag.isIrp(record.get(ColumnConstants.OPR))) return true;
          break;
        }
        case FSF: {
          if (OprFlag.isFSF(record.get(ColumnConstants.OPR))) return true;
          break;
        }
        case FIO: {
          if (OprFlag.isFIO(record.get(ColumnConstants.OPR))) return true;
          break;
        }
      }
    }
    return false;
  }

  @Override
  public void setFilterOptions(Object options) {
    if (options.getClass() != OprOption[].class) return;
    this.oprOption = (OprOption[]) options;
  }
}
