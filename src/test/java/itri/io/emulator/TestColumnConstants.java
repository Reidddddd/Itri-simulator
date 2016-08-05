package itri.io.emulator;

import org.junit.Assert;
import org.junit.Test;

public class TestColumnConstants {
  @Test
  public void assertConstants() {
    Assert.assertTrue(ColumnConstants.OPR.equals("Opr"));
    Assert.assertTrue(ColumnConstants.SEQ_NUM.equals("SeqNum"));
    Assert.assertTrue(ColumnConstants.PRE_OP_TIME.equals("PreOp Time"));
    Assert.assertTrue(ColumnConstants.POST_OP_TIME.equals("PostOp Time"));
    Assert.assertTrue(ColumnConstants.PROCESS_THRD.equals("Process.Thrd"));
    Assert.assertTrue(ColumnConstants.MAJOR_OP.equals("MajorOperation"));
    Assert.assertTrue(ColumnConstants.IRP_FLAGS.equals("IrpFlags"));
    Assert.assertTrue(ColumnConstants.DEV_OBJ.equals("DevObj"));
    Assert.assertTrue(ColumnConstants.FILE_OBJ.equals("FileObj"));
    Assert.assertTrue(ColumnConstants.STATUS.equals("status"));
    Assert.assertTrue(ColumnConstants.LENGTH.equals("Length"));
    Assert.assertTrue(ColumnConstants.OFFSET.equals("Offset"));
    Assert.assertTrue(ColumnConstants.BUFFER.equals("Buffer"));
    Assert.assertTrue(ColumnConstants.OTHER1.equals("Other1"));
    Assert.assertTrue(ColumnConstants.OTHER2.equals("Other2"));
    Assert.assertTrue(ColumnConstants.OTHER3.equals("Other3"));
    Assert.assertTrue(ColumnConstants.OTHER4.equals("Other4"));
    Assert.assertTrue(ColumnConstants.PRE_OP_SYSTIME.equals("PreOp SystemTime"));
    Assert.assertTrue(ColumnConstants.POST_OP_SYSTIME.equals("PostOp SystemTime"));
    Assert.assertTrue(ColumnConstants.NAME.equals("Name"));
  }
}
