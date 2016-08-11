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
			
			avgIntervalTime = avgIntervalTime + (nowTime - lastAccessTime - avgIntervalTime) / accessCount;
			lastAccessTime = nowTime;
		} else
			lastAccessTime = nowTime;
		accessCount++;
	}
	public String toString(){
		return "accessCount: " + accessCount+" lastAccessTime: "+lastAccessTime +" avgIntervalTime: "+ avgIntervalTime;
	}
	@Override
	 public int compareTo(BlockWithTemporalLocality o) {
	    return Long.compare(this.avgIntervalTime, o.avgIntervalTime);
	 }

}
