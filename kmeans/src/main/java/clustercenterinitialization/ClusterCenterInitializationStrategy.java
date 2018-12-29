package clustercenterinitialization;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;

/**
 * Abstract class for Cluster Center Initialization Strategies.
 */
public abstract class ClusterCenterInitializationStrategy {
  public static final ClusterCenterInitializationStrategy DEFAULT = new FirstKClusterCenterInitializationStrategy();
  public static final ClusterCenterInitializationStrategy FIRST_K = new FirstKClusterCenterInitializationStrategy();

  /**
   * Computes initial centers for a k-means algorithm.
   *
   * @param dataPoints all the data points
   * @param k          the number of initial cluster centers wanted
   * @param distance   the distance strategy
   * @return k initial cluster centers
   */
  public abstract double[][] initialize(@Nonnull double[][] dataPoints, int k, @Nonnull DistanceStrategy distance);
}
