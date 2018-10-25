package kmeans.lloyd;

import java.util.Arrays;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Lloyd KMeans Strategy.
 */
public class LloydKMeansStrategy implements KMeansStrategy {
  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      int maxNumberOfIterations, @Nonnull DistanceStrategy distance) {

    int N = dataPoints.length;

    int[] clusterAssignments = new int[N]; // maps data point indices
    double[][] clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);

    boolean hasChanged = true;
    int numberOfIterations = 0;

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // step 1: assign each point to its nearest cluster
      for (int n = 0; n < N; n++) {
        clusterAssignments[n] = closestClusterIndex(dataPoints[n], clusterCenters, distance);
      }

      // step 2: assign each cluster to the average of its points
      hasChanged = Util.updateClusterCenters(clusterCenters, clusterCenters, clusterAssignments, dataPoints);

      numberOfIterations++;
    }

    final Cluster[] clusters = Arrays.stream(clusterCenters).map(Cluster::new).toArray(Cluster[]::new);
    for (int n = 0; n < N; n++) {
      clusters[clusterAssignments[n]].closestPoints.add(dataPoints[n]);
    }
    return clusters;
  }

  /**
   * Computes the index of the cluster that is closest to a given point.
   *
   * @param point          the point for which we want the closest cluster index
   * @param clusterCenters the cluster centers
   * @param distance       the distance strategy
   * @return the index of the closest cluster
   */
  private int closestClusterIndex(@Nonnull double[] point, double[][] clusterCenters, DistanceStrategy distance) {
    int closestClusterIndex = -1;
    double minDistance = Double.MAX_VALUE;
    for (int k = 0; k < clusterCenters.length; k++) {
      var currentDistance = distance.compute(point, clusterCenters[k]);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
        closestClusterIndex = k;
      }
    }
    return closestClusterIndex;
  }
}
