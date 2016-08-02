package itri.io.emulator.experiment;

import itri.io.emulator.gen.FakeFileInfo.FileSize;

public class BlocksManager {
  private Block[] blocks;

  public BlocksManager(FileSize size) {
    int len = (int) (size.getSize() % Block.UNIT_4K == 0 ? size.getSize() : size.getSize() + 1);
    blocks = new Block[len];
    for (int i = 0; i < blocks.length; i++) {
      blocks[i] = new Block(i);
    }
  }

  public void updateBlocksFrequency(long offset, int length) {
    int startBlock = (int) (offset / Block.UNIT_4K);
    int endBlock = (int) ((offset + length) / Block.UNIT_4K);
    for (int i = startBlock; i <= endBlock; i++) {
      blocks[i].updateFrequency();
    }
  }

  public Block[] getBlocks() {
    return blocks;
  }

  public int getBlocksSize() {
    return blocks.length;
  }
}
