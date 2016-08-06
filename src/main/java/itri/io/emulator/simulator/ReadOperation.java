package itri.io.emulator.simulator;

import itri.io.emulator.para.OperationInfo;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadOperation extends Operation {
  private byte[] bytes2Read;

  public ReadOperation(OperationInfo info) {
    super(info);
    bytes2Read = new byte[length];
  }

  @Override
  public void operate(RandomAccessFile file) {
    try {
      file.seek(offset);
      if (!isSync) {
        file.read(bytes2Read);
      } else {
        synchronized (file) {
          file.read(bytes2Read);
        }
      }
    } catch (IOException e) {
      System.out.println("Read error happens at " + offset + ", should read　" + length + " bytes.");
    }
  }
}
