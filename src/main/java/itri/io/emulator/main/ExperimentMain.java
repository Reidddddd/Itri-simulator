package itri.io.emulator.main;

import itri.io.emulator.cleaner.FilterOption.IrpOption;
import itri.io.emulator.cleaner.FilterOption.MajorOpOption;
import itri.io.emulator.cleaner.FilterOption.OprOption;
import itri.io.emulator.cleaner.FilterOption.StatusOption;
import itri.io.emulator.cleaner.IrpFlagFilter;
import itri.io.emulator.cleaner.KeywordFilter;
import itri.io.emulator.cleaner.MajorOpFilter;
import itri.io.emulator.cleaner.OperationTypeFilter;
import itri.io.emulator.cleaner.StatusFilter;
import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Configuration;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.experiment.BlockFrequencyExperiment;
import itri.io.emulator.experiment.BlockTemporalLocalityExperiment;
import itri.io.emulator.experiment.GraphExperiment;
import itri.io.emulator.experiment.GraphExperimentsManager;

public class ExperimentMain {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Please input the location of configuration file.");
      System.exit(0);
    }

    Configuration conf = new Configuration(args[0]);
    Parameters params = new Parameters(conf);

    BlockFrequencyExperiment blockFrequencyExperiment = new BlockFrequencyExperiment();
    addBlockFrequencyFilters(blockFrequencyExperiment, params);
    
    //block temporal locality experiment
    BlockTemporalLocalityExperiment blockTemporalLocalityExperiment = new BlockTemporalLocalityExperiment();
    addBlockFrequencyFilters(blockTemporalLocalityExperiment, params);
    
    GraphExperimentsManager manager =
        new GraphExperimentsManager(params, ColumnConstants.getColumnsHeader());
    manager.addExperiment(blockTemporalLocalityExperiment);

    manager.initialize();
    System.out.println("Pre process is done.");
    manager.run();
    System.out.println("Process is done.");
    manager.draw();
    System.out.println("Post process is done.");
  }

  private static void addBlockFrequencyFilters(GraphExperiment experiment, Parameters params) {
    OperationTypeFilter oprFilter = new OperationTypeFilter(params);
    OprOption[] oprOptions = { OprOption.IRP };
    oprFilter.setFilterOptions(oprOptions);

    KeywordFilter keywordFilter = new KeywordFilter(params);

    MajorOpFilter mjoFilter = new MajorOpFilter(params);
    MajorOpOption[] mjrOption = { MajorOpOption.IRP_READ };
    mjoFilter.setFilterOptions(mjrOption);

    StatusFilter statusFilter = new StatusFilter(params);
    StatusOption[] statusOptions = { StatusOption.SUCCESS };
    statusFilter.setFilterOptions(statusOptions);

    IrpFlagFilter irpFilter = new IrpFlagFilter(params);
    IrpOption[] irpOptions = { IrpOption.ALL };
    irpFilter.setFilterOptions(irpOptions);

    experiment.addPreProcessFilter(oprFilter);
    experiment.addPreProcessFilter(keywordFilter);
    experiment.addPreProcessFilter(mjoFilter);
    experiment.addPreProcessFilter(statusFilter);
    experiment.addPreProcessFilter(irpFilter);

    experiment.addProcessFilter(oprFilter);
    experiment.addProcessFilter(keywordFilter);
    experiment.addProcessFilter(mjoFilter);
    experiment.addProcessFilter(statusFilter);
    experiment.addProcessFilter(irpFilter);
  }
}
