package itri.io.simulator.para;

public class OpSequence {
  private long opSequence;
  
  public OpSequence(String opSeq) {
    opSequence = Long.decode(opSeq);
  }
  
  public long getOpSequence() {
    return opSequence;
  }
  
  @Override
  public String toString() {
    return String.valueOf(opSequence);
  }
}
