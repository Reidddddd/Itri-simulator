package itri.io.emulator.main;

import itri.io.emulator.common.Configuration;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.simulator.LogSimulator;

public class SimulatorMain {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Please input the location of configuration file.");
      System.exit(0);
    }
    Configuration conf = new Configuration(args[0]);
    Parameters params = new Parameters(conf);
    LogSimulator simulator = new LogSimulator(params);
    simulator.simulate();
  }
}
