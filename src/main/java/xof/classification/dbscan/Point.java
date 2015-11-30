package xof.classification.dbscan;

import xof.classification.utils.DistanceMath;
import xof.classification.utils.Pair;

public class Point {
	private Pair<Double, Double> position;
	private Point_Status status;
	private int clusterID;
	private int lable;
	
	public Point(Pair<Double, Double> position,int lable){
		this.position = position;
		this.lable = lable;
		this.status = Point_Status.NOT_VISITED;
		this.clusterID = -1;
	}
	
	public Pair<Double, Double> getPosition() {
		return position;
	}

	public void setPosition(Pair<Double, Double> position) {
		this.position = position;
	}

	public Point_Status getStatus() {
		return status;
	}

	public void setStatus(Point_Status status) {
		this.status = status;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public int getLable() {
		return lable;
	}

	public void setLable(int lable) {
		this.lable = lable;
	}
	
	public enum Point_Status{
		NOT_VISITED,NOISE,CLUSTED;
	}
	
	public double getDistance(Point other){
		return Math.sqrt(DistanceMath.calculateDistance2(position, other.getPosition()));
	}
	
	@Override
	public String toString(){
		return String.format("<%s,%d>", position.toString(),lable);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
	
	public static void main(String[] args) {
//		Point p1 = new Point(new Pair<Double, Double>(1.0, 1.0),1);
//		Point p2 = new Point(new Pair<Double, Double>(1.0, 1.0),2);

	}
}
