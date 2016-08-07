package itri.io.emulator;

import itri.io.emulator.common.Configuration;
import itri.io.emulator.common.Parameters;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestParameters {
  private static Parameters params;

  @BeforeClass
  public static void initialize() throws Exception {
    Configuration conf = new Configuration("test-site.xml");
    params = new Parameters(conf);
  }

  @Test
  public void testDirectory() throws Exception {
    Assert.assertTrue(params.getIOLogInputLocation().equals("d:\\download"));
    Assert.assertTrue(params.getReplayLogOutputLocation().equals("d:\\download\\replay"));
    Assert.assertTrue(params.getFakeFilesLocation().equals("/home/reidchan/desktop"));
  }

  @Test
  public void testFilters() throws Exception {
    String[] irps = { "c", "p", "s", "y" };
    Assert.assertArrayEquals(params.getIrpNames(), irps);
    String[] oprs = { "irp", "fio" };
    Assert.assertArrayEquals(params.getOprNames(), oprs);
    String[] mjrs = { "read", "write" };
    Assert.assertArrayEquals(params.getMajorNames(), mjrs);
    String[] status = { "success", "error" };
    Assert.assertArrayEquals(params.getStatusNames(), status);
    String[] names = { "d:\\download\\test" };
    Assert.assertArrayEquals(params.getKeyWordNames(), names);
    String[] pids = { "111c", "4" };
    Assert.assertArrayEquals(params.getProcessNames(), pids);
  }

  @Test
  public void testOthers() throws Exception {
    Assert.assertEquals(params.getBufferSize(), 10000);
  }
}
