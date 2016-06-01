package itri.io.simulator;

import java.util.Iterator;

public class ConditionManager implements Iterable<Conditions> {
  private final static int CONDITION_COUNT = 5;

  private Conditions[] conditions;
  private int index;

  public ConditionManager() {
    conditions = new Conditions[CONDITION_COUNT];
    index = 0;
  }

  public void addGroupByCondition(GroupByOption.Option... groupBy) {
    conditions[index++] = Conditions.createConditions(groupBy);
  }

  public void addFilterOprCondition(FilterOption.OprOption... oprOptions) {
    conditions[index++] = Conditions.createConditions(oprOptions);
  }
  
  public void addFilterIrpCondition(FilterOption.IrpOption... irpOptions) {
    conditions[index++] = Conditions.createConditions(irpOptions);
  }

  public void addFilterMajorOpCondition(FilterOption.MajorOpOption... majorOpOptions) {
    conditions[index++] = Conditions.createConditions(majorOpOptions);
  }

  public void addFilterStatusCondition(FilterOption.StatusOption... statusOptions) {
    conditions[index++] = Conditions.createConditions(statusOptions);
  }
  
  public int getFiltersNumber() {
    return CONDITION_COUNT - 1;
  }

  @Override
  public Iterator<Conditions> iterator() {
    return new ConditionIterator();
  }

  class ConditionIterator implements Iterator<Conditions> {
    private int index;
    
    public ConditionIterator() {
      index = 0;
    }

    @Override
    public boolean hasNext() {
      if (index < CONDITION_COUNT) return true;
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
