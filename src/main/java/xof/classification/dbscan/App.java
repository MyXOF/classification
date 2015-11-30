package xof.classification.dbscan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xof.classification.configuration.ClassificationConfig;
import xof.classification.utils.ResultBuilder;


public class App {
	public final static Logger logger = LoggerFactory.getLogger(App.class);
	
	private ExecutorService executor;
	private double epsStart;
	private double epsEnd;
	private double epsStep;
	private int minptsStart;
	private int minptsEnd;
	private int minptsStep;

	private BufferedWriter bufferedWriter;
	
	public App(){
		String epsValues[] = ClassificationConfig.getInstance().DBScan_Eps.trim().split(",");
		epsStart = Double.parseDouble(epsValues[0]);
		epsEnd = Double.parseDouble(epsValues[1]);
		epsStep = Double.parseDouble(epsValues[2]);
		
		String minptsValues[] = ClassificationConfig.getInstance().DBScan_MinPts.trim().split(",");
		minptsStart = Integer.parseInt(minptsValues[0]);
		minptsEnd = Integer.parseInt(minptsValues[1]);
		minptsStep = Integer.parseInt(minptsValues[2]);
		
		executor = Executors.newCachedThreadPool();
	}
	
	public void service(){
		String resultPath = ClassificationConfig.getInstance().DBScan_ResultPath;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(resultPath));
			
			for(double i = epsStart;i <= epsEnd;i+=epsStep){
				ArrayList<Future<String>> result = new ArrayList<Future<String>>();
				for(int j = minptsStart; j <= minptsEnd;j+=minptsStep){
					result.add(executor.submit(new DBScanThread(i, j)));
				}
				
				for(Future<String> fs : result){
					bufferedWriter.write(fs.get());
					bufferedWriter.flush();
				}
			}
		} catch (IOException e) {
			logger.error("App: failed to open file {}",resultPath,e);
		} catch (InterruptedException e) {
			logger.error("App: errors occur when get results from task",e);
		} catch (ExecutionException e) {
			logger.error("App: errors occur when get results from task",e);
		}
		executor.shutdown();
	}
	
	class DBScanThread implements Callable<String>{
		private double eps;
		private int minPts;
		
		public DBScanThread(double eps,int minPts) {
			this.eps = eps;
			this.minPts = minPts;
		}

		@Override
		public String call() throws Exception {
			long startTime = System.currentTimeMillis();
			DBScanAlgorithm algorithm = new DBScanAlgorithm(eps, minPts);
			algorithm.init();
			algorithm.cluster();
			long costTime = System.currentTimeMillis() - startTime;
			return ResultBuilder.generateResult("Eps = "+eps+" MinPts = "+minPts+" It costs "+costTime+"ms\n", algorithm.analyzeRersult());
		}		
	}
	
	public static void main(String[] args) {
		App app = new App();
		app.service();
	}

}
