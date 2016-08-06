package itri.io.emulator;

import itri.io.emulator.cleaner.FilterOption;

import java.util.Iterator;

public class ConditionManager implements Iterable<Conditions> {
  private final static int CONDITION_COUNT = 6;

  private Conditions[] conditions;
  private int index;
  private int size;

  public ConditionManager() {
    conditions = new Conditions[CONDITION_COUNT];
    index = 0;
    size = 0;
  }

  public void addGroupByCondition(GroupByOption.Option[] groupBy) {
    conditions[index++] = Conditions.createConditions(groupBy);
    size = index;
  }

  public void addFilterOprCondition(FilterOption.OprOption[] oprOptions) {
    conditions[index++] = Conditions.createConditions(oprOptions);
    size = index;
  }
  
  public void addFilterIrpCondition(FilterOption.IrpOption[] irpOptions) {
    conditions[index++] = Conditions.createConditions(irpOptions);
    size = index;
  }

  public void addFilterMajorOpCondition(FilterOption.MajorOpOption[] majorOpOptions) {
    conditions[index++] = Conditions.createConditions(majorOpOptions);
    size = index;
  }

  public void addFilterStatusCondition(FilterOption.StatusOption[] statusOptions) {
    conditions[index++] = Conditions.createConditions(statusOptions);
    size = index;
  }
  
  public void addFilterNameCondition(String[] nameOptions) {
    conditions[index++] = Conditions.createConditions(nameOptions);
    size = index;
  }
  
  public int getFiltersNumber() {
    return size - 1;
  }

  @Override
  public Iterator<Conditions> iterator() {
    return new ConditionIterator();
  }

  public class ConditionIterator implements Iterator<Conditions> {
    private int index;
    
    public ConditionIterator() {
      index = 0;
    }

    @Override
    public boolean hasNext() {
      if (index < size) return true;
      return false;
    }

    @Override
    public Conditions next() {
      return conditions[index++];
    }
    
    public void reset() {
      index = 0;
    }
  }
}
