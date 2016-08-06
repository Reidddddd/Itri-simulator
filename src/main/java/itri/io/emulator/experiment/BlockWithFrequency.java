package itri.io.emulator.experiment;

public class BlockWithFrequency extends Block implements Comparable<BlockWithFrequency> {
  private long frequency;

  public BlockWithFrequency(long id) {
    super(id);
    this.frequency = 0;
  }

  public void updateFrequency() {
    this.frequency++;
  }

  public long getFrequency() {
    return frequency;
  }

  @Override
  public int compareTo(BlockWithFrequency o) {
    return Long.compare(this.frequency, o.frequency);
  }
}
