package itri.io.emulator.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class Filter {
	private static String DELIMETER = ",";
	public String logPath;
	public BufferedReader reader;
	public OutputStream out;
	public FilterCondition filter = null;

	public Filter(String logPath, FilterCondition filter) {
		this.logPath = logPath;
		this.filter = filter;
	}

	public void startFilter() {
		String line = null;
		String splited[] = null;
		try {
			if (open()) {
				while ((line = reader.readLine()) != null) {
					splited = trimedArrays(line);
					if (splited.length == 20){
						if (filter.filter(splited))
							write(line);
					}
					
				
				}

			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			close();
		}
	}

	protected boolean open() throws FileNotFoundException {
		boolean fileOpenSuccess = true;
		try {
			reader = new BufferedReader(new FileReader(logPath));
			out = new FileOutputStream(logPath + "_filter");
		} catch (FileNotFoundException e) {
			System.err.println("Can not find the file in the path: " + logPath);
			fileOpenSuccess = false;
			throw new FileNotFoundException(e.getMessage());
		}
		return fileOpenSuccess;
	}

	protected void write(String line) throws IOException {
		out.write((line + "\r\n").getBytes());
	}

	protected void close() {
		try {
			if (reader != null)
				reader.close();
			if (out != null)
				out.close();
		} catch (IOException e) {
			System.err.println("Error occurs when close the file in the path: " + logPath);
		}
	}

	protected String[] trimedArrays(String line) {
		String[] trimed = line.split(DELIMETER);
		for (int i = 0; i < trimed.length; i++) {
			trimed[i] = trimed[i].trim();
		}
		return trimed;
	}
}
