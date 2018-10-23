package clusterCenterInitialization;

import java.util.Arrays;

/**
 * First K Cluster Center Initialization Strategy.
 */
public class FirstKClusterCenterInitializationStrategy implements ClusterCenterInitializationStrategy {
  @Override
  public double[][] initialize(double[][] dataPoints, int K) {
    var result = new double[K][];
    for (int k = 0; k < K; k++) {
      result[k] = Arrays.copyOf(dataPoints[k], dataPoints[k].length);
    }
    return result;
  }
}
