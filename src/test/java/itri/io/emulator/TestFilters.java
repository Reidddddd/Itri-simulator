package itri.io.emulator;

import itri.io.emulator.FilterOption.IrpOption;
import itri.io.emulator.FilterOption.MajorOpOption;
import itri.io.emulator.FilterOption.OprOption;
import itri.io.emulator.FilterOption.StatusOption;
import itri.io.emulator.cleaner.DefaultFilter;
import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.cleaner.IrpFlagFilter;
import itri.io.emulator.cleaner.KeywordFilter;
import itri.io.emulator.cleaner.MajorOpFilter;
import itri.io.emulator.cleaner.OperationTypeFilter;
import itri.io.emulator.cleaner.StatusFilter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFilters {
  private Filter filter;
  private static Parameters params;
  private static CSVParser parser;

  @BeforeClass
  public static void initialize() throws Exception {
    Configuration conf = new Configuration("test-site.xml");
    params = new Parameters(conf);
    parser =
        CSVParser.parse(new File("test.csv"), Charset.defaultCharset(),
          CSVFormat.DEFAULT.withHeader(ColumnConstants.getColumnsHeader()));
  }

  @Test
  public void testOperationTypeFilter() throws IOException {
    filter = new OperationTypeFilter(params);
    Assert.assertTrue(passedRecords(filter) == 9);
    filter.setFilterOptions(new OprOption[] { OprOption.IRP });
    Assert.assertTrue(passedRecords(filter) == 6);
    filter.setFilterOptions(new OprOption[] { OprOption.FIO });
    Assert.assertTrue(passedRecords(filter) == 3);
    filter.setFilterOptions(new OprOption[] { OprOption.FSF });
    Assert.assertTrue(passedRecords(filter) == 1);
    filter.setFilterOptions(new OprOption[] { OprOption.IRP, OprOption.FSF, OprOption.FIO });
    Assert.assertTrue(passedRecords(filter) == 10);
  }

  @Test
  public void testMajorOpFilter() throws IOException {
    filter = new MajorOpFilter(params);
    Assert.assertTrue(passedRecords(filter) == 9);
    filter.setFilterOptions(new MajorOpOption[] { MajorOpOption.IRP_READ });
    Assert.assertTrue(passedRecords(filter) == 4);
    filter.setFilterOptions(new MajorOpOption[] { MajorOpOption.IRP_WRITE });
    Assert.assertTrue(passedRecords(filter) == 5);
    filter.setFilterOptions(new MajorOpOption[] { MajorOpOption.IRP_OTHER });
    Assert.assertTrue(passedRecords(filter) == 1);
  }

  @Test
  public void testStatusFilter() throws IOException {
    filter = new StatusFilter(params);
    Assert.assertTrue(passedRecords(filter) == 10);
    filter.setFilterOptions(new StatusOption[] { StatusOption.SUCCESS });
    Assert.assertTrue(passedRecords(filter) == 9);
    filter.setFilterOptions(new StatusOption[] { StatusOption.WARNING });
    Assert.assertTrue(passedRecords(filter) == 0);
    filter.setFilterOptions(new StatusOption[] { StatusOption.ERROR });
    Assert.assertTrue(passedRecords(filter) == 1);
  }

  @Test
  public void testKeywordFilter() throws IOException {
    filter = new KeywordFilter(params);
    Assert.assertTrue(passedRecords(filter) == 8);
    filter.setFilterOptions(new String[] { "d:\\download\\test2" });
    Assert.assertTrue(passedRecords(filter) == 2);
    filter.setFilterOptions(new String[] { "d:\\download\\test", "d:\\download\\test2" });
    Assert.assertTrue(passedRecords(filter) == 10);
  }

  @Test
  public void testIrpFlagFilter() throws IOException {
    filter = new IrpFlagFilter(params);
    Assert.assertTrue(passedRecords(filter) == 10);
    filter.setFilterOptions(new IrpOption[] { IrpOption.ALL });
    Assert.assertTrue(passedRecords(filter) == 10);
    filter.setFilterOptions(new IrpOption[] { IrpOption.CACHED });
    Assert.assertTrue(passedRecords(filter) == 7);
    filter.setFilterOptions(new IrpOption[] { IrpOption.SYNC_API });
    Assert.assertTrue(passedRecords(filter) == 6);
    filter.setFilterOptions(new IrpOption[] { IrpOption.PAGING_IO });
    Assert.assertTrue(passedRecords(filter) == 3);
    filter.setFilterOptions(new IrpOption[] { IrpOption.SYNC_PAGING_IO });
    Assert.assertTrue(passedRecords(filter) == 2);
  }

  @Test
  public void testDefaultFilter() throws IOException {
    filter = new DefaultFilter();
    Assert.assertTrue(passedRecords(filter) == 10);
  }

  private int passedRecords(Filter filter) throws IOException {
    int passed = 0;
    List<CSVRecord> records = parser.getRecords();
    for (CSVRecord record : records) {
      if (filter.filter(record)) passed++;
    }
    return passed;
  }

  @AfterClass
  public static void cleanUp() throws IOException {
    parser.close();
  }
}
