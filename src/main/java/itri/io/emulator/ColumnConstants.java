package itri.io.emulator;

public class ColumnConstants {
  public final static String OPR = "Opr";
  public final static String SEQ_NUM = "SeqNum";
  public final static String PRE_OP_TIME = "PreOpTime";
  public final static String POST_OP_TIME = "PostOpTime";
  public final static String PROCESS_THRD = "Process.Thrd";
  public final static String MAJOR_OP = "MajorOperation";
  public final static String IRP_FLAGS = "IrpFlags";
  public final static String DEV_OBJ = "DevObj";
  public final static String FILE_OBJ = "FileObj";
  public final static String STATUS = "status";
  public final static String LENGTH = "Length";
  public final static String OFFSET = "Offset";
  public final static String BUFFER = "Buffer";
  public final static String OTHER1 = "Other1";
  public final static String OTHER2 = "Other2";
  public final static String OTHER3 = "Other3";
  public final static String OTHER4 = "Other4";
  public final static String PRE_OP_SYSTIME = "PreOpSystemTime";
  public final static String POST_OP_SYSTIME = "PostOpSystemTime";
  public final static String NAME = "Name";

  public static String[] getColumnsHeader() {
    String[] header =
        { OPR, SEQ_NUM, PRE_OP_TIME, POST_OP_TIME, PROCESS_THRD, MAJOR_OP, IRP_FLAGS, DEV_OBJ,
            FILE_OBJ, STATUS, LENGTH, OFFSET, BUFFER, OTHER1, OTHER2, OTHER3, OTHER4,
            PRE_OP_SYSTIME, POST_OP_SYSTIME, NAME };
    return header;
  }
}
