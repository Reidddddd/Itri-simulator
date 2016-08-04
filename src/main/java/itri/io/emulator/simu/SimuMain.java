package itri.io.emulator.simu;

import itri.io.emulator.GroupByOption;
import itri.io.emulator.Parameters;
import itri.io.emulator.util.Configuration;

public class SimuMain {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Please input the location of configuration file.");
      System.exit(0);
    }
    Configuration conf = new Configuration(args[0]);
    Parameters params = new Parameters(conf);
    GroupByOption groupBy = new GroupByOption(params.getMergeKeyWord());
    LogSimulator simulator = LogSimulator.createSimulator(groupBy.getGroupByType(), params);

    System.out.println("Start to simulate!");
    simulator.simulate(params.getFakeFilesLocation());
  }
}
