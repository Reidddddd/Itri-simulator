package itri.io.simulator.util;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class FileDirectoryFactory {
  private static HashMap<String, String> fileNameMap = new HashMap<>();
  private static HashMap<String, String> fileNumMap = new HashMap<>();
  private static Formatter fmt;
  private static int fileNumber = 1;
  
  public static void makeDir(String dirName) {
    File dir = new File(dirName);
    if (!dir.exists()) dir.mkdir();
  }
  
  public static File createNewFile(String fileName) throws IOException {
    File file = new File(fileName);
    if (!file.exists()) file.createNewFile();
    return file;
  }
  
  private static String getFileNumber() {
    fmt = new Formatter();
    fmt.format("%05d", fileNumber++);
    return fmt.toString();
  }
  
  public static String extractAlpName(String fileName) {
    String alpFileName = fileNameMap.get(fileName);
    if (alpFileName == null) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < fileName.length(); i++) {
        if (Character.isLetter(fileName.charAt(i))) {
          builder.append(fileName.charAt(i));
        }
      }
      String fn = builder.toString();
      fileNameMap.put(fileName, fn);
      fileNumMap.put(fn, getFileNumber());
      return fileNumMap.get(fn) + fn;
    }
    return fileNumMap.get(alpFileName) + alpFileName;
  }
  
  /**
   * 
   */
  
  public static String generateOutputFileName(String fileName) {
    int sepIndex = StringUtils.lastIndexOf(fileName, File.separator);
    return StringUtils.substring(fileName, sepIndex + 1);
  }
  
  public static String generateNameFromWindows(String fileName) {
    int sepIndex = StringUtils.lastIndexOf(fileName, "\\");
    return StringUtils.substring(fileName, sepIndex + 1);
  }
  
  public static String generateNameFromUnix(String fileName) {
    int sepIndex = StringUtils.lastIndexOf(fileName, "/");
    return StringUtils.substring(fileName, sepIndex + 1);
  }
  
  public static String getWorkingDirectory() {
    return System.getProperty("user.dir") + File.separator + System.currentTimeMillis();
  }
}
