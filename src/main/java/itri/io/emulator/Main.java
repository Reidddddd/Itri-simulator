package itri.io.emulator;

import itri.io.emulator.gen.FileGenerator;
import itri.io.emulator.observer.CreateFlusher;
import itri.io.emulator.observer.Flusher;
import itri.io.emulator.simu.LogSimulator;

import java.io.FileInputStream;

public class Main {
  @SuppressWarnings({ "rawtypes" })
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Please input the location of configuration file.");
      System.exit(0);
    }
    Configuration conf = new Configuration(args[0]);
    Parameters params = new Parameters(conf);
    ColumnName colName = new ColumnName();
    colName.readFileAndSetColName(new FileInputStream(params.getIOLogInputLocation()));
    IndexInfo.Builder builder = new IndexInfo.Builder(colName);
    IndexInfo info = builder.setOprIndex().setSeqNumIndex()
                            .setPreOpTimeIndex().setPostOpTimeIndex()
                            .setProcessThrdIndex().setIrpFlagIndex()
                            .setStatusIndex().setMajorOpIndex()
                            .setLengthIndex().setOffsetIndex()
                            .setNameIndex()
                            .build();
    
    GroupByOption groupBy = new GroupByOption(params.getMergeKeyWord());
    LogCleaner cleaner = LogCleaner.createGenerator(groupBy.getGroupByType(),
                                                    params.getIOLogInputLocation(),
                                                    info);
    
    Flusher appender = CreateFlusher.createObserver(groupBy.getGroupByType(), params);
    FileGenerator generator = new FileGenerator(params.getFakeFilesLocation(), info);
    try {
      cleaner.addObserver(appender);
      cleaner.addObserver(generator);
      System.out.println("Replay Logs are being created...");
      cleaner.generate(params);
    } finally {
      appender.flush();
      System.out.println("Replay Logs are done.\n");
      System.out.println("Fake Files are being created...");
      generator.flush();
      System.out.println("Fake Files are done.\n");
      System.exit(0);
    }
    System.out.println("Start IO Simulation...");
    LogSimulator simulator = LogSimulator.createSimulator(groupBy.getGroupByType(), params);
    simulator.simulate(params.getFakeFilesLocation());
    System.out.println("IO Simulation is done.\n");
    System.exit(0);
  }
}
