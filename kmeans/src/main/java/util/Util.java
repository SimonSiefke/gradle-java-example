package util;

import java.util.List;

/**
 * Utility class for KMeans methods.
 */
public final class Util {
  public static double[] averageOfPoints(List<double[]> points) {
    if (points.isEmpty()) {
      return null;
    }
    final double[] sum = new double[points.get(0).length];
    for (var point : points) {
      for (int dimension = 0; dimension < point.length; dimension++) {
        var coordinateValue = point[dimension];
        sum[dimension] += coordinateValue;
      }
    }
    for (int i = 0; i < sum.length; i++) {
      sum[i] /= points.size();
    }
    return sum;
  }

  private Util() {
    // private constructor to prevent creating objects of this class.
  }
}
