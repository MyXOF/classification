package xof.classification.kmeans;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xof.classification.utils.DistanceMath;
import xof.classification.utils.Pair;

public class KMeans_Cluster {
	private static final Logger logger = LoggerFactory.getLogger(KMeans_Cluster.class);
	
	public List<Pair<Double, Double>> points;

	private Pair<Double, Double> centroid = null;
	
	public KMeans_Cluster(){
		points = new ArrayList<Pair<Double,Double>>();
		centroid = null;
	}
	
	public KMeans_Cluster(Pair<Double, Double> point){
		points = new ArrayList<Pair<Double,Double>>();
		this.centroid = point;
	}
	
	public int getSize(){
		if(points != null) return points.size();
		logger.warn("Cluster: cluster is null");
		return -1;
	}
	
	public Pair<Double, Double> getCentroid() {
		return centroid;
	}
	
	public void addPoint(Pair<Double, Double> point){
		if(points == null) {
			points = new ArrayList<Pair<Double,Double>>();
		}
		points.add(point);
	}

	public Pair<Double, Double> updateCentroid(){
		if(points == null || points.size() == 0){
			logger.warn("Cluster: cluster is null");
			return null;
		}
		double postion_x = 0;
		double postion_y = 0;
		int num = points.size();
		for(Pair<Double, Double> point : points){
			postion_x += point.left;
			postion_y += point.right;
		}
		if(centroid == null){
			centroid = new Pair<Double, Double>(postion_x/num, postion_y/num);
		}
		else{
			centroid.left = postion_x/num;
			centroid.right = postion_y/num;
		}
		return centroid;
	}
	
	public void clearPointSet(){
		if(points == null) {
			points = new ArrayList<Pair<Double,Double>>();
			return;
		}
		points.clear();
	}
	
	public Double getDistanceFromCentroid(Pair<Double, Double> point){
		if(centroid == null || point == null){
			logger.warn("Cluster: centroid or point is null");
			return -1.0;
		}
		return DistanceMath.calculateDistance2(centroid, point);
	}
	
	@Override
	public String toString(){
		if(points != null){
			return points.toString();
		}
		return "no data";
	}
	
	public List<Pair<Double, Double>> getPoints() {
		return points;
	}
	
	public static void main(String[] args) {

	}

}
