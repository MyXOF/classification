package xof.classification.utils;

public class FScoreMath {
	public static double getFScore(double recall,double precision){
		return (2*recall*precision) / (recall+precision);
	} 
}
