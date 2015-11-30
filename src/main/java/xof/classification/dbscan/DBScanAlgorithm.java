package xof.classification.dbscan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xof.classification.dbscan.Point.Point_Status;
import xof.classification.utils.DataRecord;
import xof.classification.utils.FScoreMath;
import xof.classification.utils.Pair;

public class DBScanAlgorithm {
	private final static Logger logger = LoggerFactory.getLogger(DBScanAlgorithm.class);
	private Double Eps;
	private int minPts;
	private List<DBScan_Cluster> clusters;
	private List<Point> points;
	private Map<Pair<Double, Double>, Integer> record;
	private Map<Integer, Integer> pointsSize;

	public DBScanAlgorithm(Double Eps, int minPts) {
		this.Eps = Eps;
		this.minPts = minPts;
		clusters = new ArrayList<DBScan_Cluster>();
		points = new ArrayList<Point>();
	}

	public void init() {
		record = DataRecord.getInstance().getRecord();
		for (Map.Entry<Pair<Double, Double>, Integer> entry : record.entrySet()) {
			Pair<Double, Double> postion = new Pair<Double, Double>(entry.getKey().left, entry.getKey().right);
			points.add(new Point(postion, entry.getValue()));
		}
		pointsSize = DataRecord.getInstance().getSizeMap();
	}

	public void cluster() {
		long start = System.currentTimeMillis();
		for (Point point : points) {
			if (point.getStatus() != Point_Status.NOT_VISITED) {
				continue;
			}
			List<Point> neighbors = getNeighbors(point, points);
			if (neighbors == null)
				continue;
			if (neighbors.size() >= minPts) {
				clusters.add(expandCluster(point, neighbors, points));
			} else {
				point.setStatus(Point_Status.NOISE);
			}

		}
		db_showResult(start);
	}
	
	public void db_showResult(long start){
		logger.info("It costs {} ms",System.currentTimeMillis() - start);
		int count = 0;
		for(Point point : points){
			if(point.getStatus() != Point_Status.NOT_VISITED){
				count++;
			}
		}
		logger.info("count is {}",count);
	}
	
	public void displayResult(){
		for(DBScan_Cluster cluster : clusters){
			logger.debug(cluster.toString());
		}
		logger.debug("");
	}
	
	public Pair<Double, List<Pair<Integer, Double>>> analyzeRersult(){
		List<Pair<Integer, Double>> f_ScoreList = new ArrayList<Pair<Integer, Double>>();
		int maxSize = 0;
		for(DBScan_Cluster cluster : clusters){
			Map<Integer, Integer> pointCount = new HashMap<Integer, Integer>();
			for(Point point : cluster.getPoints()){
				int id = point.getLable();
				if(pointCount.get(id) == null){
					pointCount.put(id, 1);
				}
				else{
					int num = pointCount.get(id);
					pointCount.put(id, num+1);
				}
			}
			logger.info(pointCount.toString());
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
		logger.info("DBScanAlgorithm: purity is {}",purity);		
		logger.info("DBScanAlgorithm: f_score list is {}",f_ScoreList.toString());	
		
		return new Pair<Double, List<Pair<Integer, Double>>>(purity, f_ScoreList);
	}

	private List<Point> getNeighbors(Point point, List<Point> points) {
		List<Point> neighbors = new ArrayList<Point>();
		for (final Point neighbor : points) {
			if (point != neighbor && neighbor.getDistance(point) <= Eps) {
				neighbors.add(neighbor);
			}
		}
		logger.debug("DBScanAlgorithm: point {} has {} neighbour",point.getPosition().toString(),neighbors.size());
		return neighbors;
	}

	private DBScan_Cluster expandCluster(Point point, List<Point> neighbors,List<Point> points) {
		DBScan_Cluster cluster = new DBScan_Cluster();
		cluster.addPoint(point);
		point.setStatus(Point_Status.CLUSTED);
		List<Point> seeds = new ArrayList<Point>(neighbors);

		for (int i = 0; i < seeds.size(); i++) {
			Point currentPoint = seeds.get(i);
			if (currentPoint.getStatus() == Point_Status.NOT_VISITED) {
				List<Point> currentNeighbors = getNeighbors(currentPoint,points);
				seeds = merge(seeds, currentNeighbors);
			} 
			if (currentPoint.getStatus() != Point_Status.CLUSTED) {
				currentPoint.setStatus(Point_Status.CLUSTED);
				cluster.addPoint(currentPoint);
			}
		}
		logger.info("DBScanAlgorithm: upadate {} points",seeds.size());
		return cluster;
	}

	private List<Point> merge(final List<Point> one, final List<Point> two) {
		final Set<Point> oneSet = new HashSet<Point>(one);
		for (Point item : two) {
			if (!oneSet.contains(item)) {
				one.add(item);
			}
		}
		return one;
	}

	public static void main(String[] args) {
		DBScanAlgorithm test = new DBScanAlgorithm(1.0, 7);
		test.init();
		test.cluster();
		test.analyzeRersult();
	}

}
