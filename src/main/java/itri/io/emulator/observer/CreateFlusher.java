package itri.io.emulator.observer;

import itri.io.emulator.GroupByOption;
import itri.io.emulator.Parameters;

public class CreateFlusher {

  public static Flusher createObserver(GroupByOption.Option option, Parameters params) {
    Flusher observer = null;
    switch (option) {
      case FILE_NAME: observer = new FileBasedFlusher(params.getReplayLogOutputLocation(), params.getBufferSize());      break;
      case TIME_SEQ:    observer = new TimeBasedFlusher(params.getReplayLogOutputLocation(), params.getBufferSize());   break;
      default:                 observer = new FileBasedFlusher(params.getReplayLogOutputLocation(), params.getBufferSize());      break;
    }
    return observer;
  }
}
