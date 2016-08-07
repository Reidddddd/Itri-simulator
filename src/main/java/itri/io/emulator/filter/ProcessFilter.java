package itri.io.emulator.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import itri.io.emulator.main.IndexInfo;

public class ProcessFilter extends Decorator {
	// private static String DELIMETER = ",";
	// public String logPath;
	// public BufferedReader reader;
	public IndexInfo info;
	public FilterCondition filter = null;
	public String[] processNames;

	public ProcessFilter(String[] processNames, IndexInfo info, FilterCondition filter) {
		// this.logPath = logPath;
		this.processNames = processNames;
		this.info = info;
		this.filter = filter;
	}

	public void setFilter(FilterCondition filter) {
		this.filter = filter;
	}

	@Override
	public boolean filter(String[] splited) {
		// TODO Auto-generated method stub
		boolean isLeave = false;
	
		
		for (String pid : processNames) {
			if (splited[info.getProcessThrdIndex()].contains(pid + "."))
				isLeave = true;
		}

		return filter.filter(splited) && isLeave;

	}

}
