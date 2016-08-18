package itri.io.emulator.experiment;

public class BlockWithTemporalLocality extends Block implements Comparable<BlockWithTemporalLocality>{

	private double avgIntervalTime;
	private long lastAccessTime;
	private long accessCount;
	private long totalIntervalTime;
	
	public BlockWithTemporalLocality(long id) {
		super(id);
		avgIntervalTime = 0;
		lastAccessTime = 0;
		accessCount = 0;
		totalIntervalTime = 0;
	}
	public double getAvgIntervalTime(){
		return avgIntervalTime;
	}

	public long getAccessCount(){
		return accessCount;
	}
	public void updateAvgIntervalTime(long nowTime) {
		
		if (accessCount > 0) {
			if (nowTime > lastAccessTime){
				double interval = nowTime - lastAccessTime;
				totalIntervalTime += interval;
				avgIntervalTime = avgIntervalTime + (interval - avgIntervalTime) / accessCount;
				lastAccessTime = nowTime;
			} else{
				double interval = lastAccessTime - nowTime;
				totalIntervalTime += interval;
				avgIntervalTime = avgIntervalTime + (interval - avgIntervalTime) / accessCount;
			}
			accessCount++;
		} else {
			lastAccessTime = nowTime;
			accessCount++;
		}
			
	}
	public String toString(){
		return "accessCount: " + accessCount+" lastAccessTime: "+lastAccessTime +" avgIntervalTime: "+ avgIntervalTime+" totalIntervalTime: "+totalIntervalTime;
	}
	@Override
	 public int compareTo(BlockWithTemporalLocality o) {
	    return Double.compare(this.avgIntervalTime, o.avgIntervalTime);
	 }

}
