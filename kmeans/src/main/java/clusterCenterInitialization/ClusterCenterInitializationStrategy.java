package clusterCenterInitialization;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;

/**
 * Interface for Cluster Center Initialization Strategies.
 */
public interface ClusterCenterInitializationStrategy {
  /**
   * Computes initial centers for a k-means algorithm.
   *
   * @param dataPoints all the data points
   * @param k          the number of initial cluster centers wanted
   * @param distance   the distance strategy
   * @return k initial cluster centers
   */
  double[][] initialize(@Nonnull double[][] dataPoints,  int k, @Nonnull DistanceStrategy distance);
}
