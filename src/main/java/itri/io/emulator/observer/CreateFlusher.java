package itri.io.emulator.observer;

import itri.io.emulator.GroupByOption;
import itri.io.emulator.Parameters;

public class CreateFlusher {

  public static Flusher createObserver(GroupByOption.Option option, Parameters params) {
    Flusher observer = null;
    switch (option) {
      case FILE_NAME: observer = new FileBasedFlusher(params.getOutDir(), params.getRecordSize());      break;
      case TIME_SEQ:    observer = new TimeBasedFlusher(params.getOutDir(), params.getRecordSize());   break;
      default:                 observer = new FileBasedFlusher(params.getOutDir(), params.getRecordSize());      break;
    }
    return observer;
  }
}
