package itri.io.emulator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestConfiguration {
  private static Configuration conf;

  // Directory
  private final static String LOG_PATH = "emulator.io.log.location";
  private final static String OUTPUT_DIR = "emulator.replay.log.location";
  private final static String MODFILE_DIR = "emulator.fake.file.location";

  // Filter
  private final static String FILTER_NAME = "emulator.keyword.filter";
  private final static String FILTER_IRP = "emulator.irp.filter";
  private final static String FILTER_OPR = "emulator.opr.filter";
  private final static String FILTER_MAJOR_OP = "emulator.majorop.filter";
  private final static String FILTER_STATUS = "emulator.status.filter";
  private final static String FILTER_PROCESS = "emulator.process.filter";

  private final static String RECORD_SIZE = "emulator.buffer.size";

  @BeforeClass
  public static void initialize() throws Exception {
    conf = new Configuration("test-site.xml");
  }

  @Test
  public void testDirectory() throws Exception {
    Assert.assertTrue(conf.get(LOG_PATH).equals("d:\\download"));
    Assert.assertTrue(conf.get(OUTPUT_DIR).equals("d:\\download\\replay"));
    Assert.assertTrue(conf.get(MODFILE_DIR).equals("/home/reidchan/desktop"));
  }

  @Test
  public void testFilters() throws Exception {
    String[] irps = { "c", "p", "s", "y" };
    Assert.assertArrayEquals(conf.getStrings(FILTER_IRP), irps);
    String[] oprs = { "irp", "fio" };
    Assert.assertArrayEquals(conf.getStrings(FILTER_OPR), oprs);
    String[] mjrs = { "read", "write" };
    Assert.assertArrayEquals(conf.getStrings(FILTER_MAJOR_OP), mjrs);
    String[] status = { "success", "error" };
    Assert.assertArrayEquals(conf.getStrings(FILTER_STATUS), status);
    String[] names = { "d:\\download\\test" };
    Assert.assertArrayEquals(conf.getStrings(FILTER_NAME), names);
  }

  @Test
  public void testOthers() throws Exception {
    Assert.assertEquals(conf.getInt(RECORD_SIZE, 50000), 10000);
    String[] pids = { "146", "258" };
    Assert.assertArrayEquals(conf.getStrings(FILTER_PROCESS), pids);
  }
}
