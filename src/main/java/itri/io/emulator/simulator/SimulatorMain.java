package itri.io.emulator.simulator;

import itri.io.emulator.Configuration;
import itri.io.emulator.Parameters;

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
