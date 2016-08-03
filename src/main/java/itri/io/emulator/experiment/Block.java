package itri.io.emulator.experiment;

public class Block {
  public final static long UNIT_4K = 1024 * 4;

  private long id;
  private long frequency;

  public Block(long id) {
    this.id = id;
    this.frequency = 0;
  }

  public void updateFrequency() {
    this.frequency++;
  }

  public long getId() {
    return id;
  }

  public long getFrequency() {
    return frequency;
  }
}
