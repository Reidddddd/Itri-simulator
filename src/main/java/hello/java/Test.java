package hello.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class Test {
  
  public static void main(String[] args) throws IOException {
    int a = StringUtils.lastIndexOf("/home/reidchan/Desktop/hi.txt", "/");
    System.out.println("/home/reidchan/Desktop/hi.txt".substring(a));
    System.out.println("hi");
    System.exit(0);
    BufferedReader reader = new BufferedReader(new FileReader("/home/reidchan/Public/ANSYS3.txt"));
    String line;
    int count = 0;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
      StringTokenizer token = new StringTokenizer(line, "\t");
      while (token.hasMoreElements()) {
        System.out.println(((String) token.nextElement()).trim().replace(" ", ""));
      }
      if (count++ == 10) break;
    }
    reader.close();
  }
}
