package itri.io.emulator.common;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;

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
        if (Character.isLetter(fileName.charAt(i)) || Character.isDigit(fileName.charAt(i))) {
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

  public static String extractNameOnlyLettersAndDigits(String origin) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < origin.length(); i++) {
      if (Character.isLetter(origin.charAt(i)) || Character.isDigit(origin.charAt(i))) {
        builder.append(origin.charAt(i));
      }
    }
    return builder.toString();
  }

  public static int search(File[] lists, String name) {
    String removeNumPrefix = name.substring(5);
    for (int i = 0; i < lists.length; i++) {
      if (lists[i].getName().contains(removeNumPrefix)) return i;
    }
    return -1;
  }

  public static String extractAlpNameWithoutNumPrefix(String fileName) {
    return extractAlpName(fileName).substring(5);
  }
}
