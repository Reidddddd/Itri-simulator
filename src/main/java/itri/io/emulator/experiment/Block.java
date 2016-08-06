package itri.io.emulator.experiment;

public class Block {
  public final static long UNIT_4K = 1024 * 4;

  private long id;

  public Block(long id) {
    this.id = id;
  }

  public long getBlockId() {
    return id;
  }
}
