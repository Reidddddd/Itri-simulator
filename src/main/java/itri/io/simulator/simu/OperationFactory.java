package itri.io.simulator.simu;

import itri.io.simulator.para.OperationInfo;
import itri.io.simulator.para.MajorOp.OpType;

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
