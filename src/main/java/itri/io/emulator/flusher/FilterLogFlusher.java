package itri.io.emulator.flusher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import itri.io.emulator.ColumnConstants;
import itri.io.emulator.Parameters;
import itri.io.emulator.cleaner.Filter;

public class FilterLogFlusher extends Flusher {
	private static CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator('\n');
	private static FileWriter fileWriter = null;
	private static CSVPrinter csvPrinter = null;

	private String logPath;
	private int bufferSize;
	private int currentSize;

	public FilterLogFlusher(Parameters params) {
		this.logPath = params.getIOLogInputLocation();
		this.bufferSize = params.getBufferSize();
		this.currentSize = 0;

		open();
	}

	public void open() {
		try {
			fileWriter = new FileWriter(logPath + "filter");
			csvPrinter = new CSVPrinter(fileWriter, csvFormat);
			csvPrinter.printRecords(ColumnConstants.getColumnsHeader());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			fileWriter.close();
			csvPrinter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null) {
			flush();
			deleteAndRename();
			return;
		}
		CSVRecord record = (CSVRecord) arg;
		for (Filter filter : filters) {
			if (!filter.filter(record))
				return;
		}
		try {
			csvPrinter.printRecord(record);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (++currentSize > bufferSize) {
			flush();
			currentSize = 0;
		}

	}

	@Override
	public void flush() {
		try {
			csvPrinter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteAndRename(){
		File file = new File(logPath);
		file.delete();
		File toBeRenamedFile = new File(logPath+"filter");
		toBeRenamedFile.renameTo(file);
	}

}
