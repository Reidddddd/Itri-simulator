package itri.io.simulator.simu;

import itri.io.simulator.Parameters;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class SimuMain {
  public static void main(String[] args) {
    Configuration conf = new Configuration();
    conf.addResource(new Path(args[0]));
    Parameters params = new Parameters(conf);
    
    System.out.println("Start to simulate!");
    Simulator simulator = new Simulator(new File(params.getOutDir()),
                                        params.getFileTest());
    simulator.simulate(params.getModDir());
  }
}
