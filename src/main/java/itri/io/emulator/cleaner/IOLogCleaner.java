package itri.io.emulator.cleaner;

import itri.io.emulator.Parameters;
import itri.io.emulator.flusher.FlusherType;
import itri.io.emulator.flusher.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * For I/O Log clean.
 */
public class IOLogCleaner extends Observable implements AutoCloseable {
  private List<Filter> filters = new LinkedList<>();
  private CSVParser parser;
  private String ioCsvPath;
  private String[] headers;

  public IOLogCleaner(Parameters params, String[] headers) throws IOException {
    this.ioCsvPath = params.getIOLogInputLocation();
    this.headers = headers;
  }

  public void addFilter(Filter filter) {
    filters.add(filter);
  }

  /**
   * Scan csv file from top to bottom line by line.
   */
  public void clean() {
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
      if (filters.size() == 0) filters.add(new DefaultFilter());

      int required = filters.size();
      int count = 0;
      for (CSVRecord record : parser.getRecords()) {
        // All records are passed to FakeFile Flusher where conditions are checked
        setChanged();
        notifyObservers(new Visitor(FlusherType.FAKE_FILE) {
          @Override
          public Tuple visit(CSVRecord csvRecord) {
            return new Tuple(this.type, csvRecord);
          }
        }.visit(record));

        count = 0;
        for (Filter filter : filters) {
          if (filter.filter(record)) count++;
        }

        // All filter passed, it is the record we want.
        if (count == required) {
          setChanged();
          notifyObservers(new Visitor(FlusherType.REPLAY_LOG) {
            @Override
            public Tuple visit(CSVRecord csvRecord) {
              return new Tuple(this.type, csvRecord);
            }
          }.visit(record));
        }
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void close() throws Exception {
    setChanged();
    notifyObservers();
    parser.close();
  }

  /**
   * Tuple is used to wrap the FlushType and CsvRecord together.
   * In the Flusher, the flushType can be used to distinguish between ReplayLog Flusher and FakeFile Flusher.
   */
  public class Tuple {
    FlusherType type;
    CSVRecord record;

    Tuple(FlusherType type, CSVRecord record) {
      this.type = type;
      this.record = record;
    }

    public FlusherType getFlusherType() {
      return type;
    }

    public CSVRecord getRecord() {
      return record;
    }
  }
}
