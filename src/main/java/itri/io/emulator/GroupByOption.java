package itri.io.emulator;

import org.apache.hadoop.conf.Configuration;

public class GroupByOption {
  private final static String OPTION = "groupby.option";
  private final static String FILE_NAME = "filename";
  private final static String THREAD = "thread";
  private final static String MAJOR_OP = "majorop";
  private final static String TIME_SEQ = "timesequence";

  private GroupByOption.Option groupByType;
  
  public GroupByOption(String option) {
    switch (option) {
      case FILE_NAME: groupByType = Option.FILE_NAME; break;
      case THREAD:    groupByType = Option.THREAD;    break;
      case MAJOR_OP:  groupByType = Option.MAJOR_OP;  break;
      case TIME_SEQ:  groupByType = Option.TIME_SEQ;  break;
      default:        groupByType = Option.FILE_NAME; break;
    }
  }

  public GroupByOption(Configuration conf) {
    String value = conf.get(OPTION);
    switch (value) {
      case FILE_NAME: groupByType = Option.FILE_NAME; break;
      case THREAD:    groupByType = Option.THREAD;    break;
      case MAJOR_OP:  groupByType = Option.MAJOR_OP;  break;
      case TIME_SEQ:  groupByType = Option.TIME_SEQ;  break;
      default:        groupByType = Option.FILE_NAME; break;
    }
  }

  public GroupByOption.Option getGroupByType() {
    return groupByType;
  }

  public enum Option {
    FILE_NAME,

    THREAD,

    MAJOR_OP,
    
    TIME_SEQ;
  }
}
