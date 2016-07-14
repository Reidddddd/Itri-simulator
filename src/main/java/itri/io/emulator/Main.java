package itri.io.emulator;

import itri.io.emulator.observer.Flusher;
import itri.io.emulator.observer.CreateFlusher;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class Main {
  @SuppressWarnings({ "rawtypes" })
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
    LogCleaner generator = LogCleaner.createGenerator(groupBy.getGroupByType(),
                                                          params.getLogPath(),
                                                          info);
    
    Flusher appender = CreateFlusher.createObserver(groupBy.getGroupByType(), params);
    try {
      generator.addObserver(appender);
      generator.generate(params);
    } finally {
      appender.flush();
    }
    System.exit(0);
  }
}
