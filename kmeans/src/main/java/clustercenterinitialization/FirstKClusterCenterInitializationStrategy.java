package clustercenterinitialization;

import java.util.Arrays;

import distance.DistanceStrategy;

/**
 * First K Cluster Center Initialization Strategy.
 */
public class FirstKClusterCenterInitializationStrategy extends ClusterCenterInitializationStrategy {
  @Override
  public double[][] initialize(double[][] dataPoints, int K, DistanceStrategy distance) {
    var result = new double[K][];
    for (int k = 0; k < K; k++) {
      result[k] = Arrays.copyOf(dataPoints[k], dataPoints[k].length);
    }
    return result;
  }
}
