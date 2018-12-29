package kmeans;

import java.util.Arrays;

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
   * stores for each center how far it has moved in the current iteration.
   */
  protected double[] clusterCenterMovements;
  /**
   * stores the cluster centers.
   */
  protected double[][] clusterCenters;
  /**
   * stores for each cluster how many points are assigned to it.
   */
  protected int[] clusterSizes;
  /**
   * stores for each cluster the sum of the points assigned to it (for each
   * dimension).
   */
  protected double[][] clusterSums;
  /**
   * stores the data points.
   */
  protected double[][] dataPoints;
  /**
   * stores for each data point (treated as index from 0 to N-1) to which cluster
   * it is assigned.
   */
  protected int[] dataPointsAssignments;
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
   * stores the maximum number of iterations.
   */
  protected int maxNumberOfIterations;
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

  /**
   * Moves each cluster center to the average of its points.
   */
  protected int moveCenters() {
    int furthestMovingCenterIndex = 0;
    for (int k = 0; k < K; k++) {
      if (clusterSizes[k] > 0) {
        final double[] newClusterCenter = new double[D];
        for (int d = 0; d < D; d++) {
          newClusterCenter[d] = clusterSums[k][d] / clusterSizes[k];
        }
        clusterCenterMovements[k] = this.distance.compute(clusterCenters[k], newClusterCenter);
        if (clusterCenterMovements[k] > clusterCenterMovements[furthestMovingCenterIndex]) {
          furthestMovingCenterIndex = k;
        }
        clusterCenters[k] = newClusterCenter;
      } else {
        throw new IllegalArgumentException(
            "Please provide different initial cluster centers, one or more of your initial clusters are too far away from any data point");
      }
    }
    hasChanged = clusterCenterMovements[furthestMovingCenterIndex] > 0;
    return furthestMovingCenterIndex;
  }

  /**
   * Assigns a point to a different cluster and removes it from its previous
   * assigned cluster.
   *
   * @param n - the index of the point
   * @param k - the index of the cluster
   */
  protected void assignPointToCluster(int n, int k) {
    var oldAssignment = dataPointsAssignments[n];
    if (oldAssignment != k) {
      clusterSizes[dataPointsAssignments[n]]--;
      clusterSizes[k]++;
      for (int d = 0; d < D; d++) {
        clusterSums[oldAssignment][d] -= dataPoints[n][d];
        clusterSums[k][d] += dataPoints[n][d];
      }
      dataPointsAssignments[n] = k;
    }
  }

  /**
   * The same as {@link #assignPointToCluster assignPointToCluster}, but when a
   * point is assigned to a cluster it isn't removed from another cluster.
   *
   * @param n - the index of the point
   * @param k - the index of the cluster
   */
  protected void initialAssignPointToCluster(int n, int k) {
    clusterSizes[k]++;
    for (int d = 0; d < D; d++) {
      clusterSums[k][d] += dataPoints[n][d];
    }
    dataPointsAssignments[n] = k;
  }

  /**
   * the result is an array of clusters, each cluster has a list of dataPoints
   * assigned to it as well as a cluster center.
   */
  protected Cluster[] result() {
    final Cluster[] clusters = Arrays.stream(clusterCenters).map(Cluster::new).toArray(Cluster[]::new);
    for (int n = 0; n < N; n++) {
      clusters[dataPointsAssignments[n]].closestPoints.add(dataPoints[n]);
    }
    return clusters;
  }

  /**
   * Initializes the data point assignments as well as the helper structures.
   */
  protected abstract void initialize();

  /**
   * The main loop of each kMeans Algorithm, loop until nothing has changed or the
   * maximum number of iterations has been reached.
   */
  protected void main() {
    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;
      loop();
      numberOfIterations++;
    }
  }

  /**
   * This will be called in every iteration of the main loop.
   */
  protected abstract void loop();
}
