package kmeans.hamerly;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Hamerly's KMeans Strategy.
 */
public class FastHamerlyKMeansStrategy extends KMeansStrategy {
  /**
   * stores for each point how least far away the second closest center is.
   */
  private double[] lowerBounds;
  /**
   * stores for each center half the distance to its next closest center.
   */
  private double[] closestOtherClusterDistances;
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

    this.closestOtherClusterDistances = new double[K];
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

  @Override
  protected void initialize() {
    for (int n = 0; n < N; n++) {
      int minDistanceIndex = -1;
      var minDistance = Double.MAX_VALUE;
      var secondMinDistance = Double.MAX_VALUE;
      for (int k = 0; k < K; k++) {
        var currentDistance = distance.compute(dataPoints[n], clusterCenters[k]);
        if (currentDistance < minDistance) {
          secondMinDistance = minDistance;
          minDistance = currentDistance;
          minDistanceIndex = k;
        } else if (currentDistance < secondMinDistance) {
          secondMinDistance = currentDistance;
        }
      }
      upperBounds[n] = minDistance;
      lowerBounds[n] = secondMinDistance;
      initialAssignPointToCluster(n, minDistanceIndex);
    }
  }

  @Override
  protected void loop() {
    updateClosestOtherClusterDistances();
    updateAssignments();
    moveCenters();
    updateBounds();
  }

  private void updateClosestOtherClusterDistances() {
    for (int k = 0; k < K; k++) {
      var closestOtherClusterDistance = Double.MAX_VALUE;
      for (int l = 0; l < K; l++) {
        if (l != k) {
          var currentDistance = distance.compute(clusterCenters[k], clusterCenters[l]);
          if (currentDistance < closestOtherClusterDistance) {
            closestOtherClusterDistance = currentDistance;
          }
        }
      }
      closestOtherClusterDistances[k] = closestOtherClusterDistance;
    }
  }

  protected void updateAssignments() {
    for (int n = 0; n < N; n++) {
      double m = Math.max(closestOtherClusterDistances[dataPointsAssignments[n]] / 2, lowerBounds[n]);
      if (upperBounds[n] > m) {
        upperBounds[n] = distance.compute(dataPoints[n], clusterCenters[dataPointsAssignments[n]]);
        if (upperBounds[n] > m) {
          int minDistanceIndex = 0;
          var minDistance = Double.MAX_VALUE;
          var secondMinDistance = Double.MAX_VALUE;
          for (int k = 0; k < K; k++) {
            var currentDistance = distance.compute(dataPoints[n], clusterCenters[k]);
            if (currentDistance < minDistance) {
              secondMinDistance = minDistance;
              minDistance = currentDistance;
              minDistanceIndex = k;
            } else if (currentDistance < secondMinDistance) {
              secondMinDistance = currentDistance;
            }
          }
          upperBounds[n] = minDistance;
          lowerBounds[n] = secondMinDistance;
          assignPointToCluster(n, minDistanceIndex);
        }
      }
    }
  }

  private void updateBounds() {
    // TODO combine with move centers to make it more efficient
    int mostDistanceMovedIndex = -1;
    var mostDistanceMoved = Double.MIN_VALUE;
    var secondMostDistanceMoved = Double.MIN_VALUE;
    for (int k = 0; k < K; k++) {
      var currentDistanceMoved = clusterCenterMovements[k];
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
