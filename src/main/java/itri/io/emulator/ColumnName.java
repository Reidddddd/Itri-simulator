package itri.io.emulator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

public class ColumnName {
  private final static Log LOG = LogFactory.getLog(ColumnName.class);
  
  private ArrayList<String> colNames;
  
  public ColumnName() {
    colNames = new ArrayList<>();
  }
  
  public void readFileAndSetColName(InputStream fsis) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fsis))) {
      StringTokenizer token = new StringTokenizer(reader.readLine(), "\t");
      while (token.hasMoreElements()) {
        colNames.add(((String) token.nextElement()).trim().replace(" ", ""));
      }
    } catch (IOException e) {
      LOG.error(e.getMessage());
    }
  }
  
  public void readConfAndSetColName(Configuration conf) {
    String filePath = conf.get("");
    try {
      readFileAndSetColName(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      LOG.error(e.getMessage());
      LOG.error(filePath + " is not found.");
    }
  }
  
  public int indexOf(String column) {
    return colNames.indexOf(column);
  }
  
  public String get(int index) {
    if (index < 0 || index > size()) {
      throw new IndexOutOfBoundsException();
    }
    return colNames.get(index);
  }
  
  public int size() {
    return colNames.size();
  }
}
