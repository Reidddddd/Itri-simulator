package itri.io.emulator.experiment;

import java.util.Observer;

public abstract class Experiment implements Observer {

  protected abstract void preProcess(Object obj);

  protected abstract void process(Object obj);

  protected abstract void postProcess();
}
