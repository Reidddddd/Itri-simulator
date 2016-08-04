package itri.io.emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ColumnName {
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
      System.err.println(e.getMessage());
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
