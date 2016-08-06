package itri.io.emulator.experiment;

import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;

import org.apache.commons.csv.CSVRecord;

public abstract class StateVisitor {
  ExperimentState state;

  public StateVisitor(ExperimentState state) {
    this.state = state;
  }

  abstract Tuple visit(CSVRecord record);
}
