package itri.io.emulator.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import itri.io.emulator.ColumnName;
import itri.io.emulator.IndexInfo;
import itri.io.emulator.Parameters;


public class Main {
	public static void main(String []args) throws IOException{
		Configuration conf = new Configuration();
		conf.addResource(new Path(args[0]));
		Parameter param =new Parameter(conf);
		ColumnName colName = new ColumnName();
		colName.readFileAndSetColName(new FileInputStream(param.getSrcLogPath()));
		IndexInfo.Builder builder = new IndexInfo.Builder(colName);
		IndexInfo info = builder.setOprIndex().setSeqNumIndex()
	                            .setPreOpTimeIndex().setPostOpTimeIndex()
	                            .setProcessThrdIndex().setIrpFlagIndex()
	                            .setStatusIndex().setMajorOpIndex()
	                            .setLengthIndex().setOffsetIndex()
	                            .setNameIndex()
	                            .build();		
				
	}
	
	
}
