package itri.io.simulator;

import itri.io.simulator.observer.BufferedAppender;
import itri.io.simulator.simu.Simulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class Main {
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void main(String[] args) throws IOException {
    Configuration conf = new Configuration();
    conf.addResource(new Path(args[0]));
    Parameters params = new Parameters(conf);
    ColumnName colName = new ColumnName();
    colName.readFileAndSetColName(new FileInputStream(params.getLogPath()));
    IndexInfo.Builder builder = new IndexInfo.Builder(colName);
    IndexInfo info = builder.setOprIndex().setSeqNumIndex()
                            .setPreOpTimeIndex().setPostOpTimeIndex()
                            .setProcessThrdIndex().setIrpFlagIndex()
                            .setStatusIndex().setMajorOpIndex()
                            .setLengthIndex().setOffsetIndex()
                            .setNameIndex()
                            .build();
    GroupByOption groupBy = new GroupByOption(params.getGroupBy());
    LogGenerator generator = LogGenerator.createGenerator(groupBy.getGroupByType(),
                                                          params.getLogPath(),
                                                          info);
    BufferedAppender appender = new BufferedAppender(params.getOutDir(), params.getRecordSize());
    generator.addObserver(appender);
    generator.generate();
    appender.flush();
    
    if (!params.getAfterLog()) System.exit(0);
    
    Map log = generator.getLog();
    
    System.out.println("Start to simulate!");
    Simulator simulator = new Simulator(params.getFileTest());
    long start = System.currentTimeMillis();
    simulator.simulate(log);
    System.out.println(System.currentTimeMillis() - start);
  }
}
