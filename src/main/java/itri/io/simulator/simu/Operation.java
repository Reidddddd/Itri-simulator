package itri.io.simulator.simu;

import itri.io.simulator.para.OperationInfo;

import java.io.RandomAccessFile;

public abstract class Operation {
  protected int length;
  protected long offset;
  
  public Operation(OperationInfo info) {
    this.length = info.getLength();
    this.offset = info.getOffset();
  }
  
  public abstract void operate(RandomAccessFile file);
}
