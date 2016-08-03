package itri.io.emulator.simu;

import itri.io.emulator.GroupByOption;
import itri.io.emulator.Parameters;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class SimuMain {
  public static void main(String[] args) {
    Configuration conf = new Configuration();
    conf.addResource(new Path(args[0]));
    Parameters params = new Parameters(conf);
    GroupByOption groupBy = new GroupByOption(params.getMergeKeyWord());
    LogSimulator simulator = LogSimulator.createSimulator(groupBy.getGroupByType(), params);

    System.out.println("Start to simulate!");
    simulator.simulate(params.getFakeFilesLocation());
  }
}