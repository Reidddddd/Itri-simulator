package itri.io.emulator.simu;

import itri.io.emulator.para.OperationInfo;
import itri.io.emulator.para.MajorOp.OpType;

public class OperationFactory {
  public static Operation getOperation(OperationInfo info) {
    if (info.getOpType() == OpType.READ) {
      return new ReadOperation(info);
    } else if (info.getOpType() == OpType.WRITE) {
      return new WriteOperation(info);
    } else {
      throw new IllegalStateException(info + " is unsupporeted.");
    }
  }
}
