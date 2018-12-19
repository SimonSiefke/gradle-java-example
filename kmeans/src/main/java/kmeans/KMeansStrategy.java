package kmeans;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;

/**
 * Abstract class for K-Means Strategies.
 */
public abstract class KMeansStrategy {
  /**
   * stores the number of dimensions.
   */
  protected int D;
  /**
   * stores the number of clusters.
   */
  protected int K;
  /**
   * stores the number of data points.
   */
  protected int N;

  /**
   * stores for each data point (treated as index from 0 to N-1) to which cluster
   * it is assigned.
   */
  protected int[] dataPointAssignments;
  /**
   * stores the cluster centers.
   */
  protected double[][] clusterCenters;
  /**
   * stores for each cluster the sum of the points assigned to it (for each
   * dimension).
   */
  protected double[][] clusterSums;
  /**
   * stores for each cluster how many points are assigned to it.
   */
  protected int[] clusterSizes;
  /**
   * stores the data points.
   */
  protected double[][] dataPoints;
  /**
   * stores distance strategy (e.g. euclidean distance).
   */
  protected DistanceStrategy distance;

  /**
   * stores whether any of the centers has moved in the current iteration. If not,
   * the algorithm has converged.
   */
  protected boolean hasChanged;
  /**
   * stores the number of iterations.
   */
  protected int numberOfIterations;

  /**
   * clusters the data points.
   *
   * @param dataPoints            the data points
   * @param initialClusterCenters the initial cluster center
   * @param maxNumberOfIterations the maximum number of iterations
   * @param distanceStrategy      the distance measurement method
   * @return an Array of clusters
   */
  public abstract Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      int maxNumberOfIterations, DistanceStrategy distanceStrategy);
}
