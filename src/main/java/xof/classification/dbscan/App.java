package xof.classification.dbscan;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xof.classification.configuration.ClassificationConfig;


public class App {
	public final static Logger logger = LoggerFactory.getLogger(App.class);
	
	private ExecutorService executor;
	private double epsStart;
	private double epsEnd;
	private double epsStep;
	private int minptsStart;
	private int minptsEnd;
	private int minptsStep;
	
	public App(){
		String epsValues[] = ClassificationConfig.getInstance().DBScan_Eps.trim().split(",");
		epsStart = Double.parseDouble(epsValues[0]);
		epsEnd = Double.parseDouble(epsValues[1]);
		epsStep = Double.parseDouble(epsValues[2]);
		
		String minptsValues[] = ClassificationConfig.getInstance().DBScan_MinPts.trim().split(",");
		minptsStart = Integer.parseInt(minptsValues[0]);
		minptsEnd = Integer.parseInt(minptsValues[1]);
		minptsStep = Integer.parseInt(minptsValues[2]);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
