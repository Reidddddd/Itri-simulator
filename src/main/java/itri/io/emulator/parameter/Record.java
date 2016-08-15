package itri.io.emulator.parameter;

import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.parameter.MajorOp.OpType;

import org.apache.commons.csv.CSVRecord;

/**
 * Basic unit in replay log.
 * It is composed of opr flag, operation sequence number, execution time, process id, thread id,
 * irp flag, operation status, operation type(read or write) and its read/write length and operation start offset,
 * and target file name.
 */
public class Record {
  private OprFlag oprFlag;
  private OpSequence opSequence;
  private TimeRanger ranger;
  private ProcThrd procThrd;
  private IrpFlag irpFlag;
  private Status status;
  private OperationInfo operationInfo;
  private FileName fileName;

  public static final int OPINFO_TYPE = 0;
  public static final int OPINFO_LENGTH = 1;
  public static final int OPINFO_OFFSET = 2;
  public static final int OP_TIME = 3;
  public static final int OP_PROC = 4;
  public static final int OP_THRD = 5;
  public static final int OP_STATUS = 6;
  public static final int OP_IRPFLAG = 7;
  public static final int OP_SEQUENCE = 8;
  public static final int OP_OPRFLAG = 9;
  public static final int OP_FILENAME = 10;

  public Record(String oprFlag, String opSequence, String preOpTime, String pstOpTime,String preOpSysTime,String pstOpSysTime,
      String procThrd, String irpFlag, String status, String majorOp, String length, String offset,
      String fileName) {
    this.oprFlag = new OprFlag(oprFlag);
    this.opSequence = new OpSequence(opSequence);
    this.ranger = new TimeRanger(preOpTime, pstOpTime,preOpSysTime,pstOpSysTime);
    this.procThrd = new ProcThrd(procThrd);
    this.irpFlag = new IrpFlag(irpFlag);
    this.status = new Status(status);
    this.fileName = new FileName(fileName);
    if (MajorOp.isReadOp(majorOp)) {
      this.operationInfo = new OperationInfo(OpType.READ, offset, length, irpFlag);
    } else if (MajorOp.isWriteOp(majorOp)) {
      this.operationInfo = new OperationInfo(OpType.WRITE, offset, length, irpFlag);
    }
  }

  public Record(CSVRecord csvRecord) {
    this(csvRecord.get(ColumnConstants.OPR), csvRecord.get(ColumnConstants.SEQ_NUM),
      csvRecord.get(ColumnConstants.PRE_OP_TIME), csvRecord.get(ColumnConstants.POST_OP_TIME),
      csvRecord.get(ColumnConstants.PRE_OP_SYSTIME),csvRecord.get(ColumnConstants.POST_OP_SYSTIME),
      csvRecord.get(ColumnConstants.PROCESS_THRD), csvRecord.get(ColumnConstants.IRP_FLAGS),
      csvRecord.get(ColumnConstants.STATUS), csvRecord.get(ColumnConstants.MAJOR_OP),
      csvRecord.get(ColumnConstants.LENGTH), csvRecord.get(ColumnConstants.OFFSET),
      csvRecord.get(ColumnConstants.NAME));
  }

  public OperationInfo getOperationInfo() {
    return operationInfo;
  }

  public FileName getFName() {
    return fileName;
  }

  public String getFileName() {
    return fileName.getFileName();
  }

  public long getOffset() {
    return this.operationInfo.getOffset();
  }

  public int getLength() {
    return this.operationInfo.getLength();
  }
  public TimeRanger getRanger(){
	return ranger;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(operationInfo.toString() + ":").append(ranger.toString() + ":")
        .append(procThrd.toString() + ":").append(status.toString() + ":")
        .append(irpFlag.toString() + ":").append(opSequence.toString() + ":")
        .append(oprFlag.toString() + ":").append(fileName.toString() + "\n");
    return builder.toString();
  }
}
