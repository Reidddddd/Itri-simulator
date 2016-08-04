package itri.io.emulator.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import akka.event.Logging.Info;
import akka.util.Index;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.LogCleaner;

public class ProcessFilter {
	private static Log LOG = LogFactory.getLog(LogCleaner.class);
	private static String DELIMETER =",";
	public BufferedReader reader;
	public IndexInfo info;
	public ProcessCondition condition;
	public String srcLogPath;
	public String dstLogPath;
	
	public ProcessFilter(String srcLogPath,String dstLogPath,String[] processNames,IndexInfo info){
		this.srcLogPath = srcLogPath;
		this.dstLogPath = dstLogPath;
		this.info = info;
		condition = new ProcessCondition(processNames);
	}
	
	
	protected boolean open() throws FileNotFoundException {
	    boolean fileOpenSuccess = true;
	    try {
	      reader = new BufferedReader(new FileReader(srcLogPath));
	    } catch (FileNotFoundException e) {
	      LOG.error("Can not find the file in the path: " + srcLogPath);
	      fileOpenSuccess = false;
	      throw new FileNotFoundException(e.getMessage());
	    }
	    return fileOpenSuccess;
	  }
	
	public void filter(){
		String line = null;
		String splited[] = null;
		 try {
		      if (open()) {
		    	  while ( (line = reader.readLine())!=null){
		    		  splited = trimedArrays(line);
		    		  if (condition.filter(splited, info))
		    			  ;
		    	  }
		    	  
		    	  
		      }
		    } catch (IOException e) {
		      LOG.error(e.getMessage());
		    } finally {
		      close();
		    }
	}
	
	  protected void close() {
	    try {
	      if (reader != null) reader.close();
	    } catch (IOException e) {
	      LOG.error("Error occurs when close the file in the path: " + srcLogPath);
	    }
	  }
	  protected String[] trimedArrays(String line) {
		    String[] trimed = StringUtils.split(line, DELIMETER);
		    for (int i = 0; i < trimed.length; i++) {
		      trimed[i] = StringUtils.trim(trimed[i]);
		    }
		    return trimed;
		  }
}
