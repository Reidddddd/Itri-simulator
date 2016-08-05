package itri.io.emulator.para;

/**
 * Operation Sequence Number
 */
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
