package itri.io.emulator.experiment;

public class BlockWithTemporalLocality extends Block implements Comparable<BlockWithTemporalLocality>{

	private long avgIntervalTime;
	private long lastAccessTime;
	private long accessCount;
	
	public BlockWithTemporalLocality(long id) {
		super(id);
		avgIntervalTime = 0;
		lastAccessTime = 0;
		accessCount = 0;
	}
	public long getAvgIntervalTime(){
		return avgIntervalTime;
	}

	public long getAccessCount(){
		return accessCount;
	}
	public void updateAvgIntervalTime(long nowTime) {
		
		if (accessCount > 0) {
			long interval = nowTime - lastAccessTime;
			lastAccessTime = nowTime;
			avgIntervalTime = avgIntervalTime + (nowTime - avgIntervalTime) / accessCount;
		} else
			lastAccessTime = nowTime;
		accessCount++;
	}
	@Override
	 public int compareTo(BlockWithTemporalLocality o) {
	    return Long.compare(this.avgIntervalTime, o.avgIntervalTime);
	 }

}
