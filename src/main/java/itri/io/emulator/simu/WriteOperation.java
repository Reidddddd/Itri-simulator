package itri.io.emulator.simu;

import itri.io.emulator.para.OperationInfo;
import itri.io.emulator.util.RandomTools;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WriteOperation extends Operation {

  public WriteOperation(OperationInfo info) {
    super(info);
  }

  @Override
  public void operate(RandomAccessFile file) {
    byte[] ranBytes = RandomTools.generateByte(length);
    try {
      file.seek(offset);
      if (!isSync) {
        file.write(ranBytes);
      } else {
        synchronized (file) {
          file.write(ranBytes);
        }
      }
    } catch (IOException e) {
      System.out.println("Write error happens at " + offset + ", should write　" + length
          + " bytes.");
    }
  }
}
