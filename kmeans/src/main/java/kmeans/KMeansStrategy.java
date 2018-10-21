package kmeans;

/**
 * Interface for KMeans Strategies.
 */
public interface KMeansStrategy {
  /**
   * segments the input data into clusters.
   *
   * @param dataPoints            the data points
   * @param initialClusterCenters the initial cluster center
   * @param maxNumberOfIterations the maximum number of iterations
   * @return an Array of clusters
   */
  Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations);
}
