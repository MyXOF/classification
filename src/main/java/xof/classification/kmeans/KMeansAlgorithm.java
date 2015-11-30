package xof.classification.kmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xof.classification.utils.DataRecord;
import xof.classification.utils.FScoreMath;
import xof.classification.utils.Pair;

public class KMeansAlgorithm {
	private static final Logger logger = LoggerFactory.getLogger(KMeansAlgorithm.class);
	private List<KMeans_Cluster> clusters;
	private Map<Pair<Double, Double>, Integer> record;
	private Map<Integer, Integer> pointsSize;
	
	private int K_Num;
	
	public KMeansAlgorithm(int K_Num){
		this.K_Num = K_Num;
	}
	
	public boolean init(){
		if(K_Num <= 0){
			logger.error("KMeansAlgorithm: init fail, k is too small");
			return false;
		}
		record = DataRecord.getInstance().getRecord();
		pointsSize = DataRecord.getInstance().getSizeMap();
		logger.debug(pointsSize.toString());
		if(record == null || record.size() <= 0){
			logger.error("KMeansAlgorithm: init fail, no data to appply algorithm");
			return false;
		}
		if(K_Num > record.size()){
			K_Num = record.size();
			logger.warn("KMeansAlgorithm: k is bigger than the number of data.Let k = the number of data");
		}
		clusters = new ArrayList<KMeans_Cluster>();
		int count = 0;
		int index = 0;
		for(Pair<Double, Double> point : record.keySet()){
			index++;
			if(index / 3 == 0){
				continue;
			}
			Pair<Double, Double> centroid = new Pair<Double, Double>(point.left, point.right);
			clusters.add(new KMeans_Cluster(centroid));
			count++;
			if (count == K_Num) {
				break;
			}
		}
		logger.debug("KMeansAlgorithm: init sucessfully");
		return true;
	}
	
	public void service(){
		arrangePoints();
//		int count = 1;
		while (true) {
			if(isCentroidNotChanged()){
				break;
			}
			arrangePoints();
//			logger.debug("KMeansAlgorithm: update Centroid {} times",count);
//			count++;
		}
		
	}
	
	public List<KMeans_Cluster> getClusters() {
		return clusters;
	}
	
	private void arrangePoints(){
		initClusters();
		for(Pair<Double, Double> point : record.keySet()){
			Pair<Double, Integer> minDistance = new Pair<Double, Integer>(clusters.get(0).getDistanceFromCentroid(point), 0);
			for(int i = 1;i < K_Num;i++){
				double distance = clusters.get(i).getDistanceFromCentroid(point);
				if(distance < minDistance.left){
					minDistance.left = distance;
					minDistance.right = i;
				}
			}
			clusters.get(minDistance.right).addPoint(point);
		}
	}
	
	private void initClusters(){
		for(KMeans_Cluster cluster : clusters){
			cluster.clearPointSet();
		}
	}
	
	private boolean isCentroidNotChanged(){
		boolean result = true;
		for(KMeans_Cluster cluster : clusters){
			Pair<Double, Double> oldCentroid = new Pair<Double, Double>(cluster.getCentroid().left,cluster.getCentroid().right);
			result &= oldCentroid.equals(cluster.updateCentroid());			
		}	
		return result;
	}
	
	public void displayResult(){
		for(KMeans_Cluster cluster: clusters){
			logger.debug(cluster.toString());
		}
		logger.debug("");
	}
	
	public Pair<Double, List<Pair<Integer, Double>>> analyzeRersult(){
		List<Pair<Integer, Double>> f_ScoreList = new ArrayList<Pair<Integer, Double>>();
		int maxSize = 0;
		for(KMeans_Cluster cluster : clusters){
			Map<Integer, Integer> pointCount = new HashMap<Integer, Integer>();
			for(Pair<Double, Double> point : cluster.getPoints()){
				int id = record.get(point);
				if(pointCount.get(id) == null){
					pointCount.put(id, 1);
				}
				else{
					int num = pointCount.get(id);
					pointCount.put(id, num+1);
				}
			}
			logger.debug(pointCount.toString());
			int maxCount = -1;
			int maxID = -1;
			for(Map.Entry<Integer, Integer> entry : pointCount.entrySet()){
				if(entry.getValue()> maxCount){
					maxCount = entry.getValue();
					maxID = entry.getKey();
				}
			}
			maxSize += maxCount;
			double f_score = FScoreMath.getFScore((double)maxCount / cluster.getSize(), (double)maxCount / pointsSize.get(maxID));
			f_ScoreList.add(new Pair<Integer, Double>(maxID, f_score));
		}
		
		double purity = (double) maxSize / record.size();
		logger.info("KMeansAlgorithm: purity is {}",purity);		
		logger.info("KMeansAlgorithm: f_score list is {}",f_ScoreList.toString());	
		
		return new Pair<Double, List<Pair<Integer, Double>>>(purity, f_ScoreList);
	}
	


	public static void main(String[] args) {
		KMeansAlgorithm test = new KMeansAlgorithm(11);
		test.init();
		test.service();
		test.analyzeRersult();
	}

}
