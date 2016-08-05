package itri.io.emulator.flusher;

import itri.io.emulator.cleaner.IOLogCleaner.Tuple;

import org.apache.commons.csv.CSVRecord;

/**
 * Visitor pattern
 */
public abstract class Visitor {
  protected FlusherType type;

  public Visitor(FlusherType type) {
    this.type = type;
  }

  public abstract Tuple visit(CSVRecord csvRecord);
}
