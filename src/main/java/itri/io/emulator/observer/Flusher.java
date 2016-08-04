package itri.io.emulator.observer;

import java.util.Observer;

public abstract class Flusher implements Observer {
  protected String outDir;
  protected int bufferSize;
  protected int currentSize;

  public Flusher(String outDir, int bufferSize) {
    this.outDir = outDir;
    this.bufferSize = bufferSize;
  }

  public abstract void flush();
}
