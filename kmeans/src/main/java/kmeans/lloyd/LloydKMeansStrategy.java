package kmeans.lloyd;

import java.util.Arrays;
import java.util.stream.IntStream;

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

    this.dataPointsAssignments = new int[N];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.clusterCenterMovements = new double[K];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // update assignments
      for (int n = 0; n < N; n++) {
        assignPointToCluster(n, closestClusterCenterIndex(dataPoints[n]));
      }

      // update centers
      var furthestMovingCenterIndex = moveCenters();
      hasChanged = clusterCenterMovements[furthestMovingCenterIndex] > 0;

      numberOfIterations++;
    }

    return result();
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
      dataPointsAssignments[n] = closestClusterCenterIndex;
      clusterSizes[dataPointsAssignments[n]]++;
      for (int d = 0; d < D; d++) {
        clusterSums[closestClusterCenterIndex][d] += dataPoints[n][d];
      }
    }
  }

  private int closestClusterCenterIndex(double[] dataPoint) {
    int closestClusterCenterIndex = -1;
    double smallestClusterCenterDistance = Double.MAX_VALUE;

    for (int k = 0; k < K; k++) {
      double currentClusterCenterDistance = this.distance.compute(dataPoint, clusterCenters[k]);
      if (currentClusterCenterDistance < smallestClusterCenterDistance) {
        smallestClusterCenterDistance = currentClusterCenterDistance;
        closestClusterCenterIndex = k;
      }
    }
    return closestClusterCenterIndex;
  }
}
