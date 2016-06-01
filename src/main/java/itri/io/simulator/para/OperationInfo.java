package itri.io.simulator.para;

import org.apache.commons.lang.StringUtils;

import itri.io.simulator.para.MajorOp.OpType;

public class OperationInfo {
  private OpType opType;
  private long offset;
  private int length;

  public OperationInfo(OpType opType, String offset, String length) {
    this.opType = opType;
    if (opType == OpType.READ || opType == OpType.WRITE) {
      this.offset = Long.decode(offset);
      this.length = Integer.decode(length);
    } else {
      this.offset = 0;
      this.length = 0;
    }
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

  public OperationInfo(String readLine) {
    String[] records = StringUtils.split(readLine, ":");
    if (MajorOp.isReadOp(records[0])) {
      this.opType = OpType.READ;
    } else {
      this.opType = OpType.WRITE;
    }
    this.length = Integer.parseInt(records[1]);
    this.offset = Long.parseLong(records[2]);
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    return builder.append(opType + ":")
                  .append(length + ":")
                  .append(offset)
                  .toString();
  }
}
