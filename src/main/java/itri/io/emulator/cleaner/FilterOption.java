package itri.io.emulator.cleaner;

public class FilterOption {
  public enum OprOption {
    IRP,

    FSF,

    FIO;
  }

  public enum IrpOption {
    CACHED,

    PAGING_IO,

    SYNC_API,

    SYNC_PAGING_IO,

    ALL;
  }

  public enum MajorOpOption {
    IRP_ALL,

    IRP_READ,

    IRP_WRITE,

    IRP_OTHER;
  }

  public enum StatusOption {
    SUCCESS,

    WARNING,

    ERROR;
  }
}
