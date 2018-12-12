package kmeans.hamerly;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Hamerly's KMeans Strategy.
 */
public class HamerlyKMeansStrategy extends KMeansStrategy {
  /**
   * stores for each point how far away the second closest center is.
   */
  private double[] lowerBounds;
  /**
   * stores for each center how far it has moved in the current iteration.
   */
  private double[] clusterCentersDistanceMoved;
  /**
   * stores for each center half the distance to its next closest center.
   */
  private double[] closestClusterDistances;
  /**
   * stores for each point how far away its closest center maximally is.
   */
  private double[] upperBounds;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.clusterAssignments = new int[N];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.lowerBounds = new double[N];
    this.clusterCentersDistanceMoved = new double[K];
    this.closestClusterDistances = new double[K];
    this.upperBounds = new double[N];

    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;
      for (int k = 0; k < K; k++) {
        double closestClusterDistance = Double.MAX_VALUE;
        for (int l = 0; l < K; l++) {
          if (k != l) {
            closestClusterDistance = Math.min(closestClusterDistance,
                this.distance.compute(clusterCenters[k], clusterCenters[l]));
          }
        }
        closestClusterDistances[k] = closestClusterDistance;
      }

      // update assignments
      for (int n = 0; n < N; n++) {
        double m = Math.max(closestClusterDistances[clusterAssignments[n]] / 2, lowerBounds[n]);
        if (upperBounds[n] > m) {
          upperBounds[n] = this.distance.compute(dataPoints[n], clusterCenters[clusterAssignments[n]]);
          if (upperBounds[n] > m) {
            int closestClusterCenterIndex = -1;
            double closestClusterCenterDistance = Double.MAX_VALUE;
            double secondClosestClusterCenterDistance = Double.MAX_VALUE;
            for (int k = 0; k < K; k++) {
              double currentClusterCenterDistance = this.distance.compute(dataPoints[n], clusterCenters[k]);
              if (currentClusterCenterDistance < closestClusterCenterDistance) {
                secondClosestClusterCenterDistance = closestClusterCenterDistance;
                closestClusterCenterDistance = currentClusterCenterDistance;
                closestClusterCenterIndex = k;
              } else if (currentClusterCenterDistance < secondClosestClusterCenterDistance) {
                secondClosestClusterCenterDistance = currentClusterCenterDistance;
              }
            }
            upperBounds[n] = closestClusterCenterDistance;
            lowerBounds[n] = secondClosestClusterCenterDistance;
            Util.assignPointToCluster(clusterAssignments, n, closestClusterCenterIndex, clusterSizes, clusterSums, D,
                dataPoints);
          }
        }
      }

      // update centers
      for (int k = 0; k < K; k++) {
        if (clusterSizes[k] > 0) {
          final double[] newClusterCenter = new double[D];
          for (int d = 0; d < D; d++) {
            double newClusterCenterCoordinate = clusterSums[k][d] / clusterSizes[k];
            newClusterCenter[d] = newClusterCenterCoordinate;
          }
          clusterCentersDistanceMoved[k] = this.distance.compute(clusterCenters[k], newClusterCenter);
          hasChanged = hasChanged || clusterCentersDistanceMoved[k] > 0;
          System.arraycopy(newClusterCenter, 0, clusterCenters[k], 0, D);
        } else {
          throw new IllegalArgumentException(
              "Please provide different initial cluster centers, one or more of your initial clusters are too far away from any data point");
        }
      }

      // update bounds
      int mostDistanceMovedIndex = -1;
      double mostDistanceMoved = Double.MIN_VALUE;
      double secondMostDistanceMoved = Double.MIN_VALUE;
      for (int k = 0; k < K; k++) {
        double currentDistanceMoved = clusterCentersDistanceMoved[k];
        if (currentDistanceMoved > mostDistanceMoved) {
          secondMostDistanceMoved = mostDistanceMoved;
          mostDistanceMoved = currentDistanceMoved;
          mostDistanceMovedIndex = k;
        } else if (currentDistanceMoved > secondMostDistanceMoved) {
          secondMostDistanceMoved = currentDistanceMoved;
        }
      }
      for (int n = 0; n < N; n++) {
        upperBounds[n] += clusterCentersDistanceMoved[clusterAssignments[n]];
        if (mostDistanceMovedIndex == clusterAssignments[n]) {
          lowerBounds[n] -= secondMostDistanceMoved;
        } else {
          lowerBounds[n] -= mostDistanceMoved;
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

  private void initialize() {
    for (int n = 0; n < N; n++) {
      int closestClusterCenterIndex = -1;
      double closestClusterCenterDistance = Double.MAX_VALUE;
      double secondClosestClusterCenterDistance = Double.MAX_VALUE;
      for (int k = 0; k < K; k++) {
        double currentClusterCenterDistance = this.distance.compute(dataPoints[n], clusterCenters[k]);
        if (currentClusterCenterDistance < closestClusterCenterDistance) {
          secondClosestClusterCenterDistance = closestClusterCenterDistance;
          closestClusterCenterDistance = currentClusterCenterDistance;
          closestClusterCenterIndex = k;
        } else if (currentClusterCenterDistance < secondClosestClusterCenterDistance) {
          secondClosestClusterCenterDistance = currentClusterCenterDistance;
        }
      }

      clusterAssignments[n] = closestClusterCenterIndex;
      upperBounds[n] = closestClusterCenterDistance;
      lowerBounds[n] = secondClosestClusterCenterDistance;

      clusterSizes[clusterAssignments[n]]++;
      for (int d = 0; d < D; d++) {
        clusterSums[clusterAssignments[n]][d] += dataPoints[n][d];
      }
    }
  }
}
