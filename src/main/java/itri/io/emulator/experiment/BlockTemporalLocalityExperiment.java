package itri.io.emulator.experiment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.csv.CSVRecord;
import org.jfree.ui.ApplicationFrame;

import itri.io.emulator.cleaner.Filter;
import itri.io.emulator.experiment.GraphExperimentsManager.Tuple;
import itri.io.emulator.flusher.FakeFileInfo;
import itri.io.emulator.parameter.FileName;
import itri.io.emulator.parameter.FileSize;
import itri.io.emulator.parameter.Record;

public class BlockTemporalLocalityExperiment extends GraphExperiment {
	private final static String EXPERIMENT_TITLE = "Blocks Read Temporal Locality";
	private final static int INITIAL_CAPACITY = 200;
	private final static float LOAD_FACTOR = 0.75f;

	private int allSize;
	private Map<FileName, FileSize> fileMaxSize;
	private Map<FileName, BlockTemporalLocalityManager> fileBlocksManager;

	public BlockTemporalLocalityExperiment() {
		this.fileMaxSize = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
		this.fileBlocksManager = null;
		this.allSize = 0;
	}

	@Override
	public void update(Observable o, Object arg) {
	  Tuple tuple = (Tuple)arg;
	  if (tuple.getState() == ExperimentState.PRE_PROCESS){
		preProcess(tuple.getRecord());
	  } else if (tuple.getState() == ExperimentState.PROCESS){
		process(tuple.getRecord());
	  } else if (tuple.getState() == ExperimentState.PROCESS){
		postProcess();
	  }
	}

	@Override
	protected void preProcess(Object obj) {
	  CSVRecord record = (CSVRecord) obj;
	  for (Filter filter : preProcessFilters) {
	    if (!filter.filter(record)) return;
	  }
	  FakeFileInfo fake = new FakeFileInfo(record);
	  if (fileMaxSize.containsKey(fake.getFileName())) {
	    fileMaxSize.get(fake.getFileName()).updateSize(fake.getFileSize());
	  } else {
	    fileMaxSize.put(fake.getFileName(), fake.getFileSize());
	  }
	}

	@Override
	protected void process(Object obj) {
	  CSVRecord csvRecord = (CSVRecord) obj;
	  for (Filter filter : processFilters) {
	    if (!filter.filter(csvRecord)) return;
	  }
	  Record record = new Record(csvRecord);
	  if (fileBlocksManager == null) {
	    fileBlocksManager = new HashMap<>(fileMaxSize.size());
	    initial();
	  }
	  BlockTemporalLocalityManager manager = fileBlocksManager.get(record.getFName());
	  manager.updateBlocksTemporalLocality(record.getOffset(),record.getLength(),Long.valueOf(record.getRanger().getPreOpTime(false)));
	}

	private void initial() {
	  for (Map.Entry<FileName, FileSize> entry : fileMaxSize.entrySet()) {
		BlockTemporalLocalityManager manager = new BlockTemporalLocalityManager(entry.getValue());
		fileBlocksManager.put(entry.getKey(), manager);
		}
	}
	
	
	@Override
	protected void postProcess() {
		for (Map.Entry<FileName,BlockTemporalLocalityManager> entry : fileBlocksManager.entrySet()){
			allSize += entry.getValue().getBlocksSize();
		}
		BlockWithTemporalLocality[] blocks = new BlockWithTemporalLocality[allSize];
		int copyIndex = 0;
		int copyLength = 0;
		for (Map.Entry<FileName, BlockTemporalLocalityManager> entry : fileBlocksManager.entrySet()) {
		      copyLength = entry.getValue().getBlocksSize();
		      System.arraycopy(entry.getValue().getBlocks(), 0, blocks, copyIndex, copyLength);
		      copyIndex += copyLength;
		}
		Arrays.sort(blocks, Collections.reverseOrder());
		
	
	}
	
	private class BlockTemporalLocalityBarChar extends ApplicationFrame {

		public BlockTemporalLocalityBarChar(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}
		
	}

}
