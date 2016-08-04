package itri.io.emulator.filter;

import org.apache.hadoop.conf.Configuration;

public class Parameter {
	private final static String FILTER_PROCESS="emulator.process.filter";
	private final static String SRC_LOG_PATH ="emulator.io.srclog.location";
	private final static String DST_LOG_PATH ="emulator.io.log.location";
	
	private String[] processNames;
	private String srcLogPath;
	private String dstLogPath;
	public Parameter(Configuration conf){
		processNames = conf.getStrings(FILTER_PROCESS);
		srcLogPath = conf.get(SRC_LOG_PATH);
		dstLogPath = conf.get(DST_LOG_PATH);
	}
	public String[] getProcessNames(){
		return processNames;
	}
	public String getSrcLogPath(){
		return srcLogPath;
	}
	public String getDstLogPath(){
		return dstLogPath;
	}
}
