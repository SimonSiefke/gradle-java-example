package kmeans.hamerly;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Hamerly KMeans Strategy.
 */
public class HamerlyKMeansStrategy implements KMeansStrategy {
  // TODO variables in parent class
  /**
   * stores the number of dimensions.
   */
  private int D;
  /**
   * stores the number of clusters.
   */
  private int K;
  /**
   * stores the number of data points.
   */
  private int N;

  /**
   * stores for each point (treated as index from 0 to N-1) to which cluster it is
   * assigned.
   */
  private int[] clusterAssignments;
  /**
   * stores the cluster centers.
   */
  private double[][] clusterCenters;
  /**
   * stores for each cluster the sum of the points assigned to it (for each
   * dimension).
   */
  private double[][] clusterSums;
  /**
   * stores for each cluster how many points are assigned to it.
   */
  private int[] clusterSizes;
  /**
   * stores the data points.
   */
  private double[][] dataPoints;
  /**
   * stores distance strategy (e.g. euclidean distance).
   */
  private DistanceStrategy distance;
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
  private double[] s;
  /**
   * stores for each point how far away its closest center maximally is.
   */
  private double[] upperBounds;
  /**
   * stores whether any of the centers has moved in the current iteration. If not,
   * the algorithm has converged.
   */
  private boolean hasChanged;
  /**
   * stores the number of iterations.
   */
  private int numberOfIterations;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.clusterAssignments = new int[N]; // maps data point indices to cluster indices
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.lowerBounds = new double[N];
    this.clusterCentersDistanceMoved = new double[K];
    this.s = new double[K];
    this.upperBounds = new double[N];

    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      for (int k = 0; k < K; k++) {
        double closestClusterDistance = Double.MAX_VALUE;
        for (int l = 0; l < K; l++) {
          if (k != l) {
            closestClusterDistance = Math.min(closestClusterDistance,
                this.distance.compute(clusterCenters[k], clusterCenters[l]));
          }
        }
        s[k] = closestClusterDistance;
      }

      for (int n = 0; n < N; n++) {
        double m = Math.max(s[clusterAssignments[n]] / 2, lowerBounds[n]);
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
            final int oldClusterAssignment = clusterAssignments[n];
            clusterAssignments[n] = closestClusterCenterIndex;
            upperBounds[n] = closestClusterCenterDistance;
            lowerBounds[n] = secondClosestClusterCenterDistance;
            if (oldClusterAssignment != clusterAssignments[n]) {
              clusterSizes[oldClusterAssignment]--;
              clusterSizes[clusterAssignments[n]]++;
              for (int d = 0; d < D; d++) {
                clusterSums[oldClusterAssignment][d] -= dataPoints[n][d];
                clusterSums[clusterAssignments[n]][d] += dataPoints[n][d];
              }
            }
          }
        }
      }

      // TODO update this method in utils and use in lloyd and elkan
      // update centers
      final double[][] newClusterCenters = new double[K][D];
      for (int k = 0; k < K; k++) {
        if (clusterSizes[k] > 0) {
          for (int d = 0; d < D; d++) {
            double newClusterCenterCoordinate = clusterSums[k][d] / clusterSizes[k];
            hasChanged = hasChanged || clusterCenters[k][d] != newClusterCenterCoordinate;
            newClusterCenters[k][d] = newClusterCenterCoordinate;
          }
          clusterCentersDistanceMoved[k] = this.distance.compute(clusterCenters[k], newClusterCenters[k]);
        }
      }
      System.arraycopy(newClusterCenters, 0, clusterCenters, 0, K);

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
