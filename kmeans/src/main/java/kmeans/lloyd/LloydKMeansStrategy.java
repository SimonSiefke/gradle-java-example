package kmeans.lloyd;

import java.util.Arrays;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Lloyd's KMeans Strategy.
 */
public class LloydKMeansStrategy extends KMeansStrategy {
  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      int maxNumberOfIterations, @Nonnull DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.clusterAssignments = new int[N];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;

    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // update assignments
      for (int n = 0; n < N; n++) {
        int closestClusterCenterIndex = -1;
        double newClusterCenterDistance = Double.MAX_VALUE;
        for (int k = 0; k < K; k++) {
          double closestClusterCenterDistance = this.distance.compute(dataPoints[n], clusterCenters[k]);
          if (closestClusterCenterDistance < newClusterCenterDistance) {
            newClusterCenterDistance = closestClusterCenterDistance;
            closestClusterCenterIndex = k;
          }
        }
        Util.assignPointToCluster(clusterAssignments, n, closestClusterCenterIndex, clusterSizes, clusterSums, D,
            dataPoints);
      }

      // update centers
      for (int k = 0; k < K; k++) {
        if (clusterSizes[k] > 0) {
          for (int d = 0; d < D; d++) {
            double newClusterCenterCoordinate = clusterSums[k][d] / clusterSizes[k];
            hasChanged = hasChanged || clusterCenters[k][d] != newClusterCenterCoordinate;
            clusterCenters[k][d] = newClusterCenterCoordinate;
          }
        } else {
          throw new IllegalArgumentException("this is rare");
        }
      }

      numberOfIterations++;
    }

    final Cluster[] clusters = Arrays.stream(clusterCenters).map(Cluster::new).toArray(Cluster[]::new);
    for (int n = 0; n < N; n++) {
      clusters[clusterAssignments[n]].closestPoints.add(dataPoints[n]);
    }
    return clusters;
  }

  public void initialize() {
    for (int n = 0; n < N; n++) {
      int closestClusterCenterIndex = -1;
      double closestClusterCenterDistance = Double.MAX_VALUE;
      for (int k = 0; k < K; k++) {
        double currentClusterCenterDistance = this.distance.compute(dataPoints[n], clusterCenters[k]);
        if (currentClusterCenterDistance < closestClusterCenterDistance) {
          closestClusterCenterDistance = currentClusterCenterDistance;
          closestClusterCenterIndex = k;
        }
      }
      clusterAssignments[n] = closestClusterCenterIndex;
      clusterSizes[clusterAssignments[n]]++;
      for (int d = 0; d < D; d++) {
        clusterSums[closestClusterCenterIndex][d] += dataPoints[n][d];
      }
    }
  }
}
