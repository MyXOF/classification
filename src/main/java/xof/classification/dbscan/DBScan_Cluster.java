package xof.classification.dbscan;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBScan_Cluster {
	private final static Logger logger = LoggerFactory.getLogger(DBScan_Cluster.class);
	private List<Point> points;
	
	public DBScan_Cluster(){
		points = new ArrayList<Point>();
	}
	
	public void addPoint(Point point){
		if(points == null){
			points = new ArrayList<Point>();
		}
		points.add(point);
	}
	
	public int getSize(){
		if(points != null) return points.size();
		logger.warn("Cluster: cluster is null");
		return -1;
	}
	
	@Override
	public String toString(){
		if(points != null){
			return points.toString();
		}
		return "no data";
	}
	
	public List<Point> getPoints() {
		return points;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
