package itri.io.emulator.para;

import itri.io.emulator.FilterOption;

public class IrpFlag {
  private final static char NO_CACHED = 'N';
  private final static char CACHED = 'C';
  private final static char PAGING_IO = 'P';
  private final static char SYNC_API = 'S';
  private final static char SYNC_PAGING_IO = 'Y';

  private boolean isCachedIo;
  private boolean isPagingIo;
  private boolean isSyncApi;
  private boolean isSynAndPagingIo;

  public IrpFlag(String irpFlag) {
    isCachedIo = (irpFlag.charAt(0) == NO_CACHED) ? false : true;
    isPagingIo = (irpFlag.charAt(1) == PAGING_IO) ? true : false;
    isSyncApi = (irpFlag.charAt(2) == SYNC_API) ? true : false;
    isSynAndPagingIo = (irpFlag.charAt(3) == SYNC_PAGING_IO) ? true : false;
  }

  public static FilterOption.IrpOption getIrpOption(String irp) {
    FilterOption.IrpOption irpOption;
    switch (irp.toUpperCase().charAt(0)) {
      case CACHED: irpOption = FilterOption.IrpOption.CACHED; break;
      case PAGING_IO: irpOption = FilterOption.IrpOption.PAGING_IO; break;
      case SYNC_API: irpOption = FilterOption.IrpOption.SYNC_API; break;
      case SYNC_PAGING_IO: irpOption = FilterOption.IrpOption.SYNC_PAGING_IO; break;
      default: irpOption = FilterOption.IrpOption.ALL; break;
    }
    return irpOption;
  }

  public static boolean isCached(String irpFlag) {
    return (irpFlag.charAt(0) != NO_CACHED);
  }

  public static boolean isPaging(String irpFlag) {
    return (irpFlag.charAt(1) == PAGING_IO);
  }

  public static boolean isSync(String irpFlag) {
    return (irpFlag.charAt(2) == SYNC_API);
  }

  public static boolean isSyncAndPaging(String irpFlag) {
    return (irpFlag.charAt(3) == SYNC_PAGING_IO);
  }

  public boolean isCachedIo() {
    return isCachedIo;
  }

  public boolean isPagingIo() {
    return isPagingIo;
  }

  public boolean isSyncApi() {
    return isSyncApi;
  }

  public boolean isSyncAndPagingIo() {
    return isSynAndPagingIo;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (isCachedIo) builder.append(CACHED);
    if (isPagingIo) builder.append(PAGING_IO);
    if (isSyncApi) builder.append(SYNC_API);
    if (isSynAndPagingIo) builder.append(SYNC_PAGING_IO);
    return builder.toString();
  }
}
