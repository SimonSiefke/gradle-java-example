package kmeans;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;

/**
 * Interface for K-Means Strategies.
 */
public interface KMeansStrategy {
  /**
   * clusters the data points.
   *
   * @param dataPoints            the data points
   * @param initialClusterCenters the initial cluster center
   * @param maxNumberOfIterations the maximum number of iterations
   * @param distanceStrategy      the distance measurement method
   * @return an Array of clusters
   */
  Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      int maxNumberOfIterations, DistanceStrategy distanceStrategy);
}
