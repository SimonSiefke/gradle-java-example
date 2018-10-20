package kmeans;

/**
 * Interface for KMeans Strategies.
 */
public interface KMeansStrategy {
  void cluster(int numberOfIterations, double[][] dataPoints);
}
