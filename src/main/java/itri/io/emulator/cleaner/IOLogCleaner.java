package itri.io.emulator.cleaner;

import itri.io.emulator.common.Parameters;
import itri.io.emulator.flusher.Flusher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Observable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * For I/O Log clean.
 */
public class IOLogCleaner extends Observable implements AutoCloseable {
  private CSVParser parser;
  private String ioCsvPath;
  private String[] headers;

  public IOLogCleaner(Parameters params, String[] headers) throws IOException {
    this.ioCsvPath = params.getIOLogInputLocation();
    this.headers = headers;
  }

  public void addFlusher(Flusher flusher) {
    this.addObserver(flusher);
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

      Iterator<CSVRecord> iter = parser.iterator();
      CSVRecord record;
      while (iter.hasNext()) {
        record = iter.next();
        // All records are passed to Flusher.
        setChanged();
        notifyObservers(record);
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void close() throws Exception {
    parser.close();
    setChanged();
    notifyObservers();
  }
}
