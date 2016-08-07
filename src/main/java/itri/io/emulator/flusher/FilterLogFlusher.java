package itri.io.emulator.flusher;

import java.io.OutputStream;
import java.util.Observable;

import org.apache.commons.csv.CSVRecord;

import itri.io.emulator.Parameters;
import itri.io.emulator.cleaner.Filter;

public class FilterLogFlusher extends Flusher {
	
	private String logPath;
	private OutputStream output;
	public FilterLogFlusher(Parameters params){
		this.logPath = params.getIOLogInputLocation();	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		CSVRecord record = (CSVRecord) arg;
		for (Filter filter : filters){
			if (!filter.filter(record)) return;
		}
		
		
	}

	@Override
	public void flush() {
		

	}

}
