package itri.io.emulator.cleaner;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.FilterOption.IrpOption;
import itri.io.emulator.Parameters;
import itri.io.emulator.para.IrpFlag;

import org.apache.commons.csv.CSVRecord;

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
        case ALL:
          return true;
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
      }
    }
    return false;
  }
}
