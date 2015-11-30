package xof.classification.utils;

public class DistanceMath {
	public static Double calculateDistance2(Pair<Double, Double> point1,Pair<Double, Double> point2){
		return Square(point1.left - point2.left) + Square(point1.right - point2.right);
	}
	
	public static Double Square(Double num){
		return num*num;
	}
}
