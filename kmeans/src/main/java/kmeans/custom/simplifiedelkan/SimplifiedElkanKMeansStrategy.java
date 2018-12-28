package kmeans.custom.simplifiedelkan;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Elkan's KMeans Strategy.
 */
public class SimplifiedElkanKMeansStrategy extends KMeansStrategy {

  /**
   * stores for each center how far it has moved in the current iteration.
   */
  private double[] clusterCentersDistanceMoved;
  private double[][] lowerBounds;
  private boolean[] r;
  private double[] upperBounds;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.dataPointsAssignments = new int[N]; // maps data point indices to cluster indices
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.clusterCentersDistanceMoved = new double[K];
    this.dataPoints = dataPoints;
    this.distance = distance;

    this.lowerBounds = new double[N][K];
    this.r = new boolean[N];
    this.upperBounds = new double[N];

    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;
      updateClusterCenterAssignments();
      updateClusterCentersAndBounds();
      numberOfIterations++;
    }

    final Cluster[] clusters = Arrays.stream(clusterCenters).map(Cluster::new).toArray(Cluster[]::new);
    for (int n = 0; n < N; n++) {
      clusters[dataPointsAssignments[n]].closestPoints.add(dataPoints[n]);
    }
    return clusters;
  }

  private void initialize() {
    for (int n = 0; n < N; n++) {
      double minDistance = Double.MAX_VALUE;
      int closestClusterIndex = -1;
      for (int k = 0; k < K; k++) {
        var currentDistance = distance.compute(dataPoints[n], clusterCenters[k]);
        if (currentDistance < minDistance) {
          minDistance = currentDistance;
          closestClusterIndex = k;
          lowerBounds[n][k] = currentDistance;
        }
      }
      dataPointsAssignments[n] = closestClusterIndex;
      clusterSizes[dataPointsAssignments[n]]++;
      upperBounds[n] = minDistance;
      for (int d = 0; d < D; d++) {
        clusterSums[dataPointsAssignments[n]][d] += dataPoints[n][d];
      }
    }
  }

  private void updateClusterCenterAssignments() {
    // step 1
    for (int k = 0; k < K; k++) {
      double minDistance = Double.MAX_VALUE;
      for (int l = 0; l < K; l++) {
        var currentDistance = distance.compute(clusterCenters[k], clusterCenters[l]);
        if (k != l && currentDistance < minDistance) {
          minDistance = currentDistance;
        }
      }
    }

    // step 3
    for (int k = 0; k < K; k++) {
      for (int n = 0; n < N; n++) {
        if (k != dataPointsAssignments[n] && upperBounds[n] > lowerBounds[n][k]) {
          // step 3a
          double minDistance;
          if (r[n]) {
            minDistance = distance.compute(dataPoints[n], clusterCenters[dataPointsAssignments[n]]);
            r[n] = false;
          } else {
            minDistance = upperBounds[n];
          }

          // step 3b
          if (minDistance > lowerBounds[n][k]) {
            double newDistance = distance.compute(dataPoints[n], clusterCenters[k]);
            if (newDistance < minDistance) {
              upperBounds[n] = newDistance;
              Util.assignPointToCluster(dataPointsAssignments, n, k, clusterSizes, clusterSums, D, dataPoints);
            }
          }
        }
      }
    }
  }

  private void updateClusterCentersAndBounds() {
    // step 4
    for (int k = 0; k < K; k++) {
      if (clusterSizes[k] > 0) {
        for (int d = 0; d < D; d++) {
          double newClusterCenterCoordinate = clusterSums[k][d] / clusterSizes[k];
          hasChanged = hasChanged || newClusterCenterCoordinate != clusterCenters[k][d];
          clusterCenters[k][d] = newClusterCenterCoordinate;
        }
      } else {
        throw new IllegalArgumentException(
            "Please provide different initial cluster centers, one or more of your initial clusters are too far away from any data point");
      }
    }

    // step 6
    for (int n = 0; n < N; n++) {
      upperBounds[n] += clusterCentersDistanceMoved[dataPointsAssignments[n]];
      for (int k = 0; k < K; k++) {
        lowerBounds[n][k] -= clusterCentersDistanceMoved[dataPointsAssignments[n]];
      }
    }
    Arrays.fill(r, true);
  }
}
