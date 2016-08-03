package itri.io.emulator.util;

import java.util.LinkedList;

public class JVMMemoryTool {
  private static int ONEG = 1024 * 1024 * 1024;
  private static int SLEEP_TIME = 1000 * 60 * 60 * 3;

  public static void main(String[] args) {
    int factor = 1;
    if (args.length == 1) {
      factor = Integer.parseInt(args[0]);
    }
    Runtime runtime = Runtime.getRuntime();
    LinkedList<MemoryConsumption> consumption = new LinkedList<>();
    // 19109200
    // 11800000
    for (int i = 0; i < 11800000 * factor; i++) {
      consumption.add(new MemoryConsumption());
    }
    System.out.println((ONEG * factor) + "\n" + (runtime.totalMemory() - runtime.freeMemory()));
    while (true) {
      try {
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        System.out.println("Interrupted");
        while ((runtime.totalMemory() - runtime.freeMemory()) < (ONEG * factor)) {
          consumption.add(new MemoryConsumption());
        }
        continue;
      }
    }
  }

  static class MemoryConsumption {
    int a;
    int b;
    int c;
    int d;

    public MemoryConsumption() {
      a = 1;
      b = 2;
      c = 3;
      d = 4;
    }
  }
}
