package itri.io.emulator.main;

public class Main {
  private final static String FILTER = "FILTER";
  private final static String EMULATOR = "EMULATOR";
  private final static String SIMULATOR = "SIMULATOR";
  private final static String EXPERIMENT = "EXPERIMENT";

  public static void main(String[] args) throws Exception {
    if (args == null) {
      printUsage();
    } else if (args.length != 2) {
      printUsage();
    }

    String function = args[0];
    String[] otherArgs = { args[1] };
    switch (function.toUpperCase()) {
      case FILTER: break;
      case EMULATOR: EmulatorMain.main(otherArgs); break;
      case SIMULATOR: SimulatorMain.main(otherArgs); break;
      case EXPERIMENT: ExperimentMain.main(otherArgs); break;
      default: throw new UnsupportedOperationException(function + " is not supported.");
    }
  }

  private static void printUsage() {
    System.out.println("Usage: <function> <configuration file location>\n");
    System.out.println("Options for function: (case insensitive)");
    System.out.println("\tFilter: shrinks size of I/O Log by filtering out irrelevant processes log.");
    System.out.println("\tEmulator: generates replay log and fake files, then replays i/o operation");
    System.out.println("\tSimulator: replays i/o operation");
    System.out.println("\tExperiment: generates figures for analysis.");
  }
}
