package itri.io.emulator.experiment;

import itri.io.emulator.ColumnName;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.Parameters;
import itri.io.emulator.util.Configuration;

import java.io.FileInputStream;

public class ExperimentMain {
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

    ExperimentsManager manager = new ExperimentsManager(params.getIOLogInputLocation(), info);
    manager.addExperiment(new BlockFrequencyExperiment(info));
    manager.initialize(params);
    System.out.println("Initialization is Done");
    manager.run();
    System.out.println("Run is Done");
    manager.draw();
  }
}
