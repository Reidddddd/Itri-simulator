package itri.io.simulator.observer;

import java.util.Observer;

public abstract class Appender implements Observer {
  protected String outDir;
  protected int bufferSize;
  protected int currentSize;
  
  public Appender(String outDir, int bufferSize) {
    this.outDir = outDir;
    this.bufferSize = bufferSize;
  }
  
  public abstract void flush();
}
