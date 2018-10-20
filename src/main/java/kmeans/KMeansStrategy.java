package kmeans;

/**
 * Interface for KMeans Strategies.
 */
interface KMeansStrategy {
  void kMeans(int numberOfIterations, double[][] dataPoints);
}
