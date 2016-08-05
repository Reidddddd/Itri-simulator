package itri.io.emulator.para;

import org.apache.commons.csv.CSVRecord;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.para.MajorOp.OpType;

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

  public Record(String oprFlag, String opSequence, String preOpTime, String pstOpTime,
      String procThrd, String irpFlag, String status, String majorOp, String length, String offset,
      String fileName) {
    this.oprFlag = new OprFlag(oprFlag);
    this.opSequence = new OpSequence(opSequence);
    this.ranger = new TimeRanger(preOpTime, pstOpTime);
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

  public Record(String[] splited, IndexInfo info) {
    this(splited[info.getOprIndex()], splited[info.getSeqNumIndex()],
      splited[info.getPreOpTimeIndex()], splited[info.getPostOpTimeIndex()],
      splited[info.getProcessThrdIndex()], splited[info.getIrpFlagIndex()],
      splited[info.getStatusIndex()], splited[info.getMajorOpIndex()],
      splited[info.getLengthIndex()], splited[info.getOffsetIndex()],
      splited[info.getNameIndex()]);
  }

  public Record(CSVRecord csvRecord) {
    this(csvRecord.get(ColumnConstants.OPR), csvRecord.get(ColumnConstants.SEQ_NUM),
      csvRecord.get(ColumnConstants.PRE_OP_TIME), csvRecord.get(ColumnConstants.POST_OP_TIME),
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
