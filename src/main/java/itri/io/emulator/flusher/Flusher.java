package itri.io.emulator.flusher;

import itri.io.emulator.cleaner.DefaultFilter;
import itri.io.emulator.cleaner.Filter;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

/**
 * Basic Flusher
 */
public abstract class Flusher implements Observer {
  protected List<Filter> filters = new LinkedList<>();

  public Flusher() {
    filters.add(new DefaultFilter());
  }

  public void addFilter(Filter filter) {
    filters.add(filter);
  }

  public abstract void flush();
}
