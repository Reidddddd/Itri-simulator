package itri.io.emulator.para;

import itri.io.emulator.para.MajorOp.OpType;

/**
 * Operation info is composed of R/W(length, offset, isSync).
 * isSync indicates whether this operation is synchronous.
 */
public class OperationInfo {
  private OpType opType;
  private long offset;
  private int length;
  private String irpFlag;

  public OperationInfo(OpType opType, String offset, String length, String irpFlag) {
    this.opType = opType;
    if (opType == OpType.READ || opType == OpType.WRITE) {
      this.offset = Long.decode(offset);
      this.length = Integer.decode(length);
    } else {
      this.offset = 0;
      this.length = 0;
    }
    this.irpFlag = irpFlag;
  }
  
  public OperationInfo() {
  }
  
  public void setOpType(String opType) {
    if (MajorOp.isRead(opType)) this.opType = OpType.READ;
    else if (MajorOp.isWrite(opType)) this.opType = OpType.WRITE;
  }
  
  public void setOffset(String offset) {
    this.offset = Long.valueOf(offset);
  }
  
  public void setLength(String length) {
    this.length = Integer.valueOf(length);
  }
  
  public void setIrpFlag(String irpFlag) {
    this.irpFlag = irpFlag;
  }

  public OperationInfo(String readLine) {
    String[] records = readLine.split(":");
    if (MajorOp.isReadOp(records[0])) {
      this.opType = OpType.READ;
    } else {
      this.opType = OpType.WRITE;
    }
    this.length = Integer.parseInt(records[1]);
    this.offset = Long.parseLong(records[2]);
    this.irpFlag = records[7];
    records = null;
  }

  public OpType getOpType() {
    return opType;
  }

  public int getLength() {
    return length;
  }

  public long getOffset() {
    return offset;
  }

  public boolean isSync() {
    return irpFlag.contains("S") || irpFlag.contains("Y");
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    return builder.append(opType + ":")
                  .append(length + ":")
                  .append(offset)
                  .toString();
  }
}
