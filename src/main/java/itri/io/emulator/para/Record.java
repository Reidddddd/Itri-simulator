package itri.io.emulator.para;

import itri.io.emulator.IndexInfo;
import itri.io.emulator.para.MajorOp.OpType;

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

  public Record(String oprFlag,  String opSequence, String preOpTime, String pstOpTime,
                String procThrd, String irpFlag,    String status,
                String majorOp,  String length,     String offset,    String fileName) {
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
    builder.append(operationInfo.toString() + ":")
           .append(ranger.toString() + ":")
           .append(procThrd.toString() + ":")
           .append(status.toString() + ":")
           .append(irpFlag.toString() + ":")
           .append(opSequence.toString() + ":")
           .append(oprFlag.toString() + ":")
           .append(fileName.toString() + "\n");
    return builder.toString();
  }
}
