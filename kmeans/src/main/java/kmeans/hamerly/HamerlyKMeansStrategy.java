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
    this.closestClusterDistances = new double[K];
    this.clusterCenterMovements = new double[K];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.dataPointsAssignments = new int[N];
    this.distance = distance;
    this.hasChanged = true;
    this.lowerBounds = new double[N];
    this.numberOfIterations = 0;
    this.maxNumberOfIterations = maxNumberOfIterations;
    this.upperBounds = new double[N];

    initialize();
    main();
    return result();
  }

  protected void initialize() {
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
      upperBounds[n] = closestClusterCenterDistance;
      lowerBounds[n] = secondClosestClusterCenterDistance;
      initialAssignPointToCluster(n, closestClusterCenterIndex);
    }
  }

  @Override
  protected void loop() {
    for (int k = 0; k < K; k++) {
      double closestClusterDistance = Double.MAX_VALUE;
      for (int l = 0; l < K && l != k; l++) {
        closestClusterDistance = Math.min(closestClusterDistance,
            this.distance.compute(clusterCenters[k], clusterCenters[l]));
      }
      closestClusterDistances[k] = closestClusterDistance;
    }

    updateAssignments();
    moveCenters();
    updateBounds();
  }

  protected void updateAssignments() {
    for (int n = 0; n < N; n++) {
      double m = Math.max(closestClusterDistances[dataPointsAssignments[n]] / 2, lowerBounds[n]);
      if (upperBounds[n] > m) {
        upperBounds[n] = this.distance.compute(dataPoints[n], clusterCenters[dataPointsAssignments[n]]);
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
          assignPointToCluster(n, closestClusterCenterIndex);
        }
      }
    }
  }

  @Override
  protected void updateBounds() {
    int mostDistanceMovedIndex = -1;
    double mostDistanceMoved = Double.MIN_VALUE;
    double secondMostDistanceMoved = Double.MIN_VALUE;
    for (int k = 0; k < K; k++) {
      double currentDistanceMoved = clusterCenterMovements[k];
      if (currentDistanceMoved > mostDistanceMoved) {
        secondMostDistanceMoved = mostDistanceMoved;
        mostDistanceMoved = currentDistanceMoved;
        mostDistanceMovedIndex = k;
      } else if (currentDistanceMoved > secondMostDistanceMoved) {
        secondMostDistanceMoved = currentDistanceMoved;
      }
    }
    for (int n = 0; n < N; n++) {
      upperBounds[n] += clusterCenterMovements[dataPointsAssignments[n]];
      if (mostDistanceMovedIndex == dataPointsAssignments[n]) {
        lowerBounds[n] -= secondMostDistanceMoved;
      } else {
        lowerBounds[n] -= mostDistanceMoved;
      }
    }
  }
}
