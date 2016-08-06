package itri.io.emulator;

import itri.io.emulator.cleaner.FilterOption;
import itri.io.emulator.cleaner.FilterOption.MajorOpOption;
import itri.io.emulator.parameter.IrpFlag;
import itri.io.emulator.parameter.MajorOp;
import itri.io.emulator.parameter.OprFlag;
import itri.io.emulator.parameter.ProcThrd;
import itri.io.emulator.parameter.Status;

public abstract class Conditions {
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static Conditions createConditions(Object... obj) {
    Class clazz = obj[0].getClass();
    if (clazz.isAssignableFrom(GroupByOption.Option.class)) {
      return new ExtractConditions((GroupByOption.Option[]) obj);
    } else if (clazz.isAssignableFrom(FilterOption.OprOption.class)) {
      return new OprFilterConditions((FilterOption.OprOption[]) obj);
    } else if (clazz.isAssignableFrom(FilterOption.IrpOption.class)) {
      return new IrpFilterConditions((FilterOption.IrpOption[]) obj);
    } else if (clazz.isAssignableFrom(FilterOption.MajorOpOption.class)) {
      return new MajorOpFilterConditions((FilterOption.MajorOpOption[]) obj);
    } else if (clazz.isAssignableFrom(FilterOption.StatusOption.class)) {
      return new StatusFilterConditions((FilterOption.StatusOption[]) obj);
    } else if (clazz.isAssignableFrom(String.class)) { 
      return new TextFilterConditions((String[]) obj);
    } else {
      throw new RuntimeException("Your condition is not supported");
    }
  }
  
  public abstract boolean filter(String[] splited, IndexInfo info);
}

class ExtractConditions extends Conditions {
  private GroupByOption.Option type;

  public ExtractConditions(GroupByOption.Option... groupBy) {
    type = groupBy[0];
  }
  
  public String extractGroup(String[] splited, IndexInfo info) {
    switch (type) {
      case FILE_NAME: return splited[info.getNameIndex()];
      case    THREAD: return ProcThrd.getTid(splited[info.getProcessThrdIndex()]);
      case  MAJOR_OP: return splited[info.getMajorOpIndex()];
             default: return splited[info.getNameIndex()];
    }
  }

  @Override
  public boolean filter(String[] splited, IndexInfo info) {
    throw new UnsupportedOperationException("filter() is not supported in ExtractCondtions");
  }
}

class OprFilterConditions extends Conditions {
  private FilterOption.OprOption[] oprFilters;
  
  public OprFilterConditions(FilterOption.OprOption... oprFilters) {
    this.oprFilters = oprFilters;
  }

  @Override
  public boolean filter(String[] splited, IndexInfo info) {
    for (int i = 0; i < oprFilters.length; i++) {
      switch (oprFilters[i]) {
        case IRP: { if (OprFlag.isIrp(splited[info.getOprIndex()])) return true; break; }
        case FSF: { if (OprFlag.isFSF(splited[info.getOprIndex()])) return true; break; }
        case FIO: { if (OprFlag.isFIO(splited[info.getOprIndex()])) return true; break; }
      }
    }
    return false;
  }
}

class IrpFilterConditions extends Conditions {
  private FilterOption.IrpOption[] irpFilters;
  
  public IrpFilterConditions(FilterOption.IrpOption... irpFilters) {
    this.irpFilters = irpFilters;
  }

  @Override
  public boolean filter(String[] splited, IndexInfo info) {
    for (int i = 0; i < irpFilters.length; i++) {
      switch (irpFilters[i]) {
        case            ALL:                                                                 return true;
        case         CACHED: { if (IrpFlag.isCached(splited[info.getIrpFlagIndex()]))        return true; break; } 
        case      PAGING_IO: { if (IrpFlag.isPaging(splited[info.getIrpFlagIndex()]))        return true; break; } 
        case       SYNC_API: { if (IrpFlag.isSync(splited[info.getIrpFlagIndex()]))          return true; break; }
        case SYNC_PAGING_IO: { if (IrpFlag.isSyncAndPaging(splited[info.getIrpFlagIndex()])) return true; break; }
      }
    }
    return false;
  }
}

class MajorOpFilterConditions extends Conditions {
  private MajorOpOption[] majorOpFilters;
  
  public MajorOpFilterConditions(FilterOption.MajorOpOption... majorOpFilters) {
    this.majorOpFilters = majorOpFilters;
  }

  @Override
  public boolean filter(String[] splited, IndexInfo info) {
    for (int i = 0; i < majorOpFilters.length; i++) {
      switch (majorOpFilters[i]) {
        case   IRP_ALL:                                                           return true;
        case  IRP_READ: { if (MajorOp.isReadOp(splited[info.getMajorOpIndex()]))  return true; break; } 
        case IRP_WRITE: { if (MajorOp.isWriteOp(splited[info.getMajorOpIndex()])) return true; break; }
        case IRP_OTHER: { if (MajorOp.isOtherOp(splited[info.getMajorOpIndex()])) return true; break; }
      }
    }
    return false;
  }
}

class StatusFilterConditions extends Conditions {
  private FilterOption.StatusOption[] statusFilters;
  
  public StatusFilterConditions(FilterOption.StatusOption... statusFilters) {
    this.statusFilters = statusFilters;
  }

  @Override
  public boolean filter(String[] splited, IndexInfo info) {
    for (int i = 0; i < statusFilters.length; i++) {
      switch (statusFilters[i]) {
        case SUCCESS: { if (Status.isSuccess(splited[info.getStatusIndex()])) return true; break; }
        case WARNING: { if (Status.isWarning(splited[info.getStatusIndex()])) return true; break; }
        case   ERROR: { if (Status.isError(splited[info.getStatusIndex()]))   return true; break; }
      }
    }
    return false;
  }
}

class TextFilterConditions extends Conditions {
  private String[] nameOptions;
  
  public TextFilterConditions(String[] nameOptions) {
    this.nameOptions = nameOptions;
  }
  
  @Override
  public boolean filter(String[] splited, IndexInfo info) {
    for (String name : nameOptions) {
      if (splited[info.getNameIndex()].contains(name)) return true;
    }
    return false;
  }
}
