package xof.classification.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xof.classification.configuration.ClassificationConfig;

public class DataRecord {
	private final static Logger logger = LoggerFactory.getLogger(DataRecord.class);
	
	private Map<Pair<Double, Double>, Integer> record;
	private Map<Integer, Integer> sizeMap;
	
	private static final DataRecord DATA_RECORD = new DataRecord();
	public static DataRecord getInstance(){
		return DATA_RECORD;
	}
	
	public int getPosition(Pair<Double, Double> point){
		if(record == null || record.get(point) == null) return -1;
		return record.get(point);
	}
	
	private DataRecord(){
		record = new HashMap<Pair<Double,Double>, Integer>();
		sizeMap = new HashMap<Integer, Integer>();
		readData();
		caculateSize();
	}
	
	public int getRecordSize(){
		return record.size();
	}
	
	private void readData(){
		String DATA_FILE_PATH = ClassificationConfig.getInstance().DATA_PATH;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(DataRecord.class.getResourceAsStream(DATA_FILE_PATH)));
		String stringLine;
		String values[] = null;
		try {
			while((stringLine = bufferedReader.readLine()) != null){
				values = stringLine.trim().split("\t");
				record.put(new Pair<Double, Double>(Double.parseDouble(values[0]), Double.parseDouble(values[1])), Integer.parseInt(values[2]));
			}
		} catch (IOException e) {
			logger.error("DataRecord: fail to read data from {}",DATA_FILE_PATH,e);
			return;
		}
		logger.debug("the number of data set is {}",record.size());
	}
	
	private void caculateSize(){
		for(Integer id : record.values()){
			if(sizeMap.get(id) == null){
				sizeMap.put(id, 1);
			}
			else{
				int size = sizeMap.get(id);
				sizeMap.put(id, size+1);
			}
		}
	}
	
	public Map<Pair<Double, Double>, Integer> getRecord() {
		return record;
	}

	public Map<Integer, Integer> getSizeMap() {
		return sizeMap;
	}

	public static void main(String[] args) {
//		DataRecord record = DataRecord.getInstance();

	}

}
