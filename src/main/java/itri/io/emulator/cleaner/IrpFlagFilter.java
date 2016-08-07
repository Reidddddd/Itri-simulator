package itri.io.emulator.cleaner;

import itri.io.emulator.cleaner.FilterOption.IrpOption;
import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.parameter.IrpFlag;

import org.apache.commons.csv.CSVRecord;

/**
 * Irp Flag filter.
 */
public class IrpFlagFilter extends Filter {
  private IrpOption[] irpOption;

  public IrpFlagFilter(Parameters params) {
    this.irpOption = new IrpOption[params.getIrpNames().length];
    int index = 0;
    for (String irp : params.getIrpNames()) {
      irpOption[index++] = IrpFlag.getIrpOption(irp);
    }
  }

  @Override
  public boolean filter(CSVRecord record) {
    for (IrpOption irp : irpOption) {
      switch (irp) {
        case CACHED: {
          if (IrpFlag.isCached(record.get(ColumnConstants.IRP_FLAGS))) return true;
          break;
        }
        case PAGING_IO: {
          if (IrpFlag.isPaging(record.get(ColumnConstants.IRP_FLAGS))) return true;
          break;
        }
        case SYNC_API: {
          if (IrpFlag.isSync(record.get(ColumnConstants.IRP_FLAGS))) return true;
          break;
        }
        case SYNC_PAGING_IO: {
          if (IrpFlag.isSyncAndPaging(record.get(ColumnConstants.IRP_FLAGS))) return true;
          break;
        }
        case ALL: return true;
      }
    }
    return false;
  }

  @Override
  public void setFilterOptions(Object options) {
    if (options.getClass() != IrpOption[].class) return;
    this.irpOption = (IrpOption[]) options;
  }
}
