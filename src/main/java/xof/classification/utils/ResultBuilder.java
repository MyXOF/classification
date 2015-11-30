package xof.classification.utils;

import java.util.List;

public class ResultBuilder {
	public static String generateResult(String head, Pair<Double, List<Pair<Integer, Double>>> result) {
		StringBuilder builder = new StringBuilder();
		builder.append(head)
						.append("purity = " + result.left + "\n")
						.append("F-score: \n");
		for (Pair<Integer, Double> fscore : result.right) {
			builder.append("\tlabel: " + fscore.left + "\t\t\tf-score: " + fscore.right + "\n");
		}
		builder.append("\n");
		return builder.toString();
	}
}
