package xof.classification.kmeans;

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
	private int threadNum;
	private String values[];

	private BufferedWriter bufferedWriter;
	
	public App(){
		String kmeans_set = ClassificationConfig.getInstance().KMeans_Set;
		values = kmeans_set.trim().split(",");
		threadNum = values.length;
		executor = Executors.newFixedThreadPool(threadNum);		
	}

	public void service(){
		ArrayList<Future<String>> result = new ArrayList<Future<String>>();
		for(int i = 0;i < threadNum;i++){
			result.add(executor.submit(new KmeansThread(Integer.parseInt(values[i]))));
		}
		String resultPath = ClassificationConfig.getInstance().KMeans_ResultPath;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(resultPath));
			for(Future<String> fs : result){
				bufferedWriter.write(fs.get());
				bufferedWriter.flush();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			logger.error("App: failed to open file {}",resultPath,e);
		} catch (InterruptedException e) {
			logger.error("App: errors occur when get results from task",e);
		} catch (ExecutionException e) {
			logger.error("App: errors occur when get results from task",e);
		}
		executor.shutdown();
	}
	
	class KmeansThread implements Callable<String>{
		private int k_value;
		
		public KmeansThread(int k_value) {
			this.k_value = k_value;
		}
		
		@Override
		public String call() throws Exception {
			long startTime = System.currentTimeMillis();
			KMeansAlgorithm algorithm = new KMeansAlgorithm(k_value);
			algorithm.init();
			algorithm.service();
			long costTime = System.currentTimeMillis() - startTime;
			return ResultBuilder.generateResult("k = "+k_value+" It costs "+costTime+"ms\n", algorithm.analyzeRersult());
		}
	}
	
	public static void main(String[] args) {
		App app = new App();
		app.service();
	}

}
