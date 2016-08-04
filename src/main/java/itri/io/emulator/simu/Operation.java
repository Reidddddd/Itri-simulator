package itri.io.emulator.simu;

import itri.io.emulator.para.OperationInfo;

import java.io.RandomAccessFile;

public abstract class Operation {
  protected int length;
  protected long offset;
  protected boolean isSync;

  public Operation(OperationInfo info) {
    this.length = info.getLength();
    this.offset = info.getOffset();
    this.isSync = info.isSync();
  }

  public abstract void operate(RandomAccessFile file);
}
