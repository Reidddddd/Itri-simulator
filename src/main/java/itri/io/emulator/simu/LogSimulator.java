package itri.io.emulator.simu;

import java.io.File;

import itri.io.emulator.GroupByOption;
import itri.io.emulator.Parameters;

public abstract class LogSimulator {
  protected File simuDir;

  public static LogSimulator createSimulator(GroupByOption.Option option, Parameters params) {
    switch (option) {
    case FILE_NAME: return new FileBasedLogSimulator(params.getOutDir());
    case TIME_SEQ: return new TimeBasedLogSimulator(params.getOutDir());
    default: return new FileBasedLogSimulator(params.getOutDir());
    }
  }

  public LogSimulator(String simuDir) {
    this.simuDir = new File(simuDir);
  }

  public abstract void simulate(String modFileDir);
}
