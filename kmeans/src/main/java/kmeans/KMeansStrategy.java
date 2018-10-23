package kmeans;

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
   * @param distanceMethod        the distance measurement method
   * @return an Array of clusters
   */
  Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distanceMethod);
}
