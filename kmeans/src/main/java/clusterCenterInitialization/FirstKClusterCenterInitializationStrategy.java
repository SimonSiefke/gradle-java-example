package clusterCenterInitialization;

import java.util.Arrays;

import distance.DistanceStrategy;

import javax.annotation.Nonnull;

/**
 * First K Cluster Center Initialization Strategy.
 */
public class FirstKClusterCenterInitializationStrategy implements ClusterCenterInitializationStrategy {
  @Override
  public double[][] initialize(@Nonnull double[][] dataPoints, int K,@Nonnull DistanceStrategy distance) {
    var result = new double[K][];
    for (int k = 0; k < K; k++) {
      result[k] = Arrays.copyOf(dataPoints[k], dataPoints[k].length);
    }
    return result;
  }
}
