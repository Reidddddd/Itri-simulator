package itri.io.emulator.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.InvalidNameException;

import org.dom4j.DocumentException;

import itri.io.emulator.Configuration;
import itri.io.emulator.Parameters;
import itri.io.emulator.ColumnName;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.Parameters;


public class FilterMain {
	public static void main(String []args) throws IOException, DocumentException, InvalidNameException{
		Configuration conf = new Configuration(args[0]);
		Parameters params =new Parameters(conf);
		ColumnName colName = new ColumnName();
		colName.readFileAndSetColName(new FileInputStream(params.getIOLogInputLocation()) );
		IndexInfo.Builder builder = new IndexInfo.Builder(colName);
		IndexInfo info = builder.setOprIndex().setSeqNumIndex()
	                            .setPreOpTimeIndex().setPostOpTimeIndex()
	                            .setProcessThrdIndex().setIrpFlagIndex()
	                            .setStatusIndex().setMajorOpIndex()
	                            .setLengthIndex().setOffsetIndex()
	                            .setNameIndex()
	                            .build();	
		FilterCondition filterCondition = new BaseFilter();
		filterCondition = new ProcessFilter(params.getProcessNames(), info, filterCondition);
		Filter filter = new Filter(params.getIOLogInputLocation(), filterCondition);
		filter.startFilter();
				
	}
	
	
}