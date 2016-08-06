package itri.io.emulator.experiment;

import itri.io.emulator.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Observable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GraphExperimentsManager extends Observable {
  private CSVParser parser;
  private String ioCsvPath;
  private String[] headers;

  private StateVisitor visitor = null;

  public GraphExperimentsManager(Parameters params, String[] headers) {
    this.ioCsvPath = params.getIOLogInputLocation();
    this.headers = headers;
  }

  public void addExperiment(GraphExperiment exp) {
    addObserver(exp);
  }

  public void initialize() throws FileNotFoundException {
    visitor = new StateVisitor(ExperimentState.PRE_PROCESS) {
      @Override
      Tuple visit(CSVRecord record) {
        return new Tuple(record, this.state);
      }
    };
    scan(visitor);
  }

  public void run() {
    visitor = new StateVisitor(ExperimentState.PROCESS) {
      @Override
      Tuple visit(CSVRecord record) {
        return new Tuple(record, this.state);
      }
    };
    scan(visitor);
  }

  public void draw() {
    visitor = new StateVisitor(ExperimentState.POST_PROCESS) {
      @Override
      Tuple visit(CSVRecord record) {
        return new Tuple(record, this.state);
      }
    };
    setChanged();
    notifyObservers(visitor.visit(null));
  }

  private void scan(StateVisitor visitor) {
    try {
      if (parser == null) {
        parser =
            CSVParser.parse(new File(ioCsvPath), Charset.defaultCharset(),
              CSVFormat.DEFAULT.withHeader(headers));
      } else if (parser.isClosed()) {
        parser =
            CSVParser.parse(new File(ioCsvPath), Charset.defaultCharset(),
              CSVFormat.DEFAULT.withHeader(headers));
      }

      Iterator<CSVRecord> iter = parser.iterator();
      CSVRecord record;
      while (iter.hasNext()) {
        record = iter.next();
        setChanged();
        notifyObservers(visitor.visit(record));
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        parser.close();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  class Tuple {
    CSVRecord record;
    ExperimentState state;

    Tuple(CSVRecord record, ExperimentState state) {
      this.record = record;
      this.state = state;
    }

    CSVRecord getRecord() {
      return record;
    }

    ExperimentState getState() {
      return state;
    }
  }
}
