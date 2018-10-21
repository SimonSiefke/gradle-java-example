package kmeans;

/**
 * Interface for KMeans Strategies.
 */
public interface KMeansStrategy {
  Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations);
}
