package itri.io.emulator.filter;

import itri.io.emulator.IndexInfo;

public class ProcessCondition {
	private String [] processOptions;
	public ProcessCondition(String[] processOptions){
		this.processOptions = processOptions;
	}
	public boolean filter(String [] splited,IndexInfo info){
		for (String pid : processOptions){
			if (splited[info.getProcessThrdIndex()].contains(pid+".") )
				return true;
		}
		return false;
	}
}
