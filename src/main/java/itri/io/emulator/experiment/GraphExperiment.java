package itri.io.emulator.experiment;

import itri.io.emulator.cleaner.DefaultFilter;
import itri.io.emulator.cleaner.Filter;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

public abstract class GraphExperiment implements Observer {
  protected List<Filter> preProcessFilters = new LinkedList<>();
  protected List<Filter> processFilters = new LinkedList<>();

  public GraphExperiment() {
    preProcessFilters.add(new DefaultFilter());
    processFilters.add(new DefaultFilter());
  }

  protected void addPreProcessFilter(Filter filter) {
    preProcessFilters.add(filter);
  }

  protected void addProcessFilter(Filter filter) {
    processFilters.add(filter);
  }

  protected abstract void preProcess(Object obj);

  protected abstract void process(Object obj);

  protected abstract void postProcess();
}
