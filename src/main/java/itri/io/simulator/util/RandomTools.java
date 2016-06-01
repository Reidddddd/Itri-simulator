package itri.io.simulator.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomTools {
  public static Random random = new Random();
  public static Map<Integer, byte[]> cached = new HashMap<>();
  
  public static byte[] generateByte(int length) {
    if (cached.get(length) == null) {
      byte[] arrs = new byte[length];
      random.nextBytes(arrs);
      cached.put(length, arrs);
      return arrs;
    }
    return cached.get(length);
  } 
}
