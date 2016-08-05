package itri.io.emulator.cleaner;

import itri.io.emulator.FilterOption.MajorOpOption;
import itri.io.emulator.ColumnConstants;
import itri.io.emulator.Parameters;
import itri.io.emulator.para.MajorOp;

import org.apache.commons.csv.CSVRecord;

public class MajorOpFilter extends Filter {
  private MajorOpOption[] mjOption;

  public MajorOpFilter(Parameters params) {
    this.mjOption = new MajorOpOption[params.getMajorNames().length];
    int index = 0;
    for (String mj : params.getMajorNames()) {
      mjOption[index++] = MajorOp.getMajorOpOption(mj);
    }
  }

  @Override
  public boolean filter(CSVRecord record) {
    for (MajorOpOption mjo : mjOption) {
      switch (mjo) {
        case IRP_ALL:
          return true;
        case IRP_READ: {
          if (MajorOp.isReadOp(record.get(ColumnConstants.MAJOR_OP))) return true;
          break;
        }
        case IRP_WRITE: {
          if (MajorOp.isWriteOp(record.get(ColumnConstants.MAJOR_OP))) return true;
          break;
        }
        case IRP_OTHER: {
          if (MajorOp.isOtherOp(record.get(ColumnConstants.MAJOR_OP))) return true;
          break;
        }
      }
    }
    return false;
  }
}
