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

import itri.io.emulator.common.ColumnConstants;
import itri.io.emulator.common.Parameters;
import itri.io.emulator.cleaner.Filter;

public class FilterLogFlusher extends Flusher {
	private static CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\r\n")
			.withHeader(ColumnConstants.getColumnsHeader());
	private FileWriter fileWriter = null;
	private CSVPrinter csvPrinter = null;

	private String logPath;
	private int bufferSize;
	private int currentSize;

	public FilterLogFlusher(Parameters params) {
		super();
		this.logPath = params.getIOLogInputLocation();
		this.bufferSize = params.getBufferSize();
		this.currentSize = 0;
		open();
	}

	private void open() {
		if (fileWriter == null && csvPrinter == null) {
			try {
				fileWriter = new FileWriter(logPath + "filter");
				csvPrinter = new CSVPrinter(fileWriter, csvFormat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void close() {
		if (fileWriter != null && csvPrinter != null){
			try {
				fileWriter.close();
				csvPrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null) {
			flush();
			close();
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
			e.printStackTrace();
		}
		if (++currentSize > bufferSize) {
			flush();
			currentSize = 0;
		}
	}

	@Override
	public void flush() {
		if (csvPrinter != null) {
			try {
				csvPrinter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteAndRename() {
		File file = new File(logPath);
		file.delete();
		File toBeRenamedFile = new File(logPath + "filter");
		toBeRenamedFile.renameTo(file);
	}

}
