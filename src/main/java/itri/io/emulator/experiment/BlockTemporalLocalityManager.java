package itri.io.emulator.experiment;

import itri.io.emulator.parameter.FileSize;

public class BlockTemporalLocalityManager {
	private BlockWithTemporalLocality[] blocks;
	
	public BlockTemporalLocalityManager(FileSize size) {
		int len = (int) (size.getSize() / Block.UNIT_4K);
	    len = (int) (size.getSize() % Block.UNIT_4K == 0 ? len : len + 1);
	    blocks = new BlockWithTemporalLocality[len];
	    for (int i = 0; i < blocks.length; i++) {
	    blocks[i] = new BlockWithTemporalLocality(i);
	    }
	}
	
	public int getBlocksSize() {
	    return blocks.length;
	}
	
	public BlockWithTemporalLocality[] getBlocks() {
	    return blocks;
	}
	
	public void updateBlocksTemporalLocality(long offset, int length,long accessTime) {
	    int startBlock = (int) (offset / Block.UNIT_4K);
	    int endBlock = (int) ((offset + length) / Block.UNIT_4K);
	    int remainder = (int) ((offset + length) % Block.UNIT_4K);
	    if (remainder == 0) endBlock--;
	    for (int i = startBlock; i <= endBlock; i++) {
	      blocks[i].updateAvgIntervalTime(accessTime);
	    }
	}
}
