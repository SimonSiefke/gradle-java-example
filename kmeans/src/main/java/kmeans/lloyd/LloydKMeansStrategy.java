package kmeans.lloyd;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Lloyd KMeans Strategy.
 */
public class LloydKMeansStrategy implements KMeansStrategy {
  private DistanceStrategy distance;
  private int D; // number of dimensions
  private int K; // number of clusters
  private int N; // number of data points

  private int[] clusterAssignments;
  private double[][] clusterCenters;
  private double[][] dataPoints;

  private boolean hasChanged;
  private int numberOfIterations;

  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      int maxNumberOfIterations, @Nonnull DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.clusterAssignments = new int[N]; // maps data point indices
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.dataPoints = dataPoints;
    this.distance = distance;

    this.hasChanged = true;
    this.numberOfIterations = 0;

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // step 1: assign each point to its nearest cluster
      for (int n = 0; n < N; n++) {
        clusterAssignments[n] = closestClusterIndex(dataPoints[n]);
      }

      // step 2: assign each cluster to the average of its points
      updateClusterCenters();

      numberOfIterations++;
    }

    final Cluster[] clusters = Arrays.stream(clusterCenters).map(Cluster::new).toArray(Cluster[]::new);
    for (int n = 0; n < N; n++) {
      clusters[clusterAssignments[n]].closestPoints.add(dataPoints[n]);
    }
    return clusters;
  }

  private void updateClusterCenters() {
    final double[][] clusterSums = new double[K][D];
    final int[] numberOfPointsInClusters = new int[K];
    for (int n = 0; n < N; n++) {
      numberOfPointsInClusters[clusterAssignments[n]]++;
      for (int d = 0; d < D; d++) {
        clusterSums[clusterAssignments[n]][d] += dataPoints[n][d];
      }
    }

    for (int k = 0; k < K; k++) {
      if (numberOfPointsInClusters[k] > 0) {
        for (int d = 0; d < D; d++) {
          double newClusterCenterCoordinate = clusterSums[k][d] / numberOfPointsInClusters[k];
          if (clusterCenters[k][d] != newClusterCenterCoordinate) {
            clusterCenters[k][d] = newClusterCenterCoordinate;
            hasChanged = true;
          }
        }
      }
    }
  }

  /**
   * Computes the index of the cluster that is closest to a given point.
   *
   * @param point the point for which we want the closest cluster index
   * @return the index of the closest cluster
   */
  private int closestClusterIndex(@Nonnull double[] point) {
    int closestClusterIndex = -1;
    double minDistance = Double.MAX_VALUE;
    for (int k = 0; k < clusterCenters.length; k++) {
      var currentDistance = this.distance.compute(point, clusterCenters[k]);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
        closestClusterIndex = k;
      }
    }
    return closestClusterIndex;
  }
}
