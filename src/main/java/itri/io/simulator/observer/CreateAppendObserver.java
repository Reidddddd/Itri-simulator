package itri.io.simulator.observer;

import itri.io.simulator.GroupByOption;
import itri.io.simulator.Parameters;

public class CreateAppendObserver {

  public static Appender createObserver(GroupByOption.Option option, Parameters params) {
    Appender observer = null;
    switch (option) {
      case FILE_NAME: observer = new FileBasedAppender(params.getOutDir(), params.getRecordSize());      break;
      case TIME_SEQ:    observer = new TimeBasedAppender(params.getOutDir(), params.getRecordSize());   break;
      default:                 observer = new FileBasedAppender(params.getOutDir(), params.getRecordSize());      break;
    }
    return observer;
  }
}
