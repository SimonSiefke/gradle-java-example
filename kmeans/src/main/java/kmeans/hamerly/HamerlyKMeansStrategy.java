package kmeans.hamerly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Hamerly KMeans Strategy.
 */
public class HamerlyKMeansStrategy implements KMeansStrategy {
  // TODO variables in parent class
  /**
   * stores the number of dimensions
   */
  private int D;
  /**
   * stores the number of clusters
   */
  private int K;
  /**
   * stores the number of data points
   */
  private int N;

  /**
   * stores for each point (treated as index from 0 to N-1) to which cluster it is
   * assigned
   */
  private int[] clusterAssignments;
  /**
   * stores the cluster centers
   */
  private double[][] clusterCenters;
  /**
   * stores for each cluster the sum of the points assigned to it (for each
   * dimension)
   */
  private double[][] clusterSum;
  /**
   * stores for each cluster how many points are assigned to it
   */
  private int[] clusterSizes;
  /**
   * stores the data points
   */
  private double[][] dataPoints;
  /**
   * stores distance strategy (e.g. euclidean distance)
   */
  private DistanceStrategy distance;
  /**
   * stores for each point how far away the second closest center is
   */
  private double[] lowerBounds;
  /**
   * stores for each center half the distance to its next closest center
   */
  private double[] s;
  /**
   * stores for each point how far away its closest center maximally is
   */
  private double[] upperBounds;
  /**
   * stores whether any of the centers has moved in the current iteration. If not,
   * the algorithm has converged.
   */
  private boolean hasChanged;
  /**
   * stores the number of iterations
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
    this.clusterSum = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.lowerBounds = new double[N];
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
            int oldClusterAssignment = clusterAssignments[n];
            // point all centers here
            if (oldClusterAssignment != clusterAssignments[n]) {
              // updateq
            }
          }
        }
      }

      // move centers

      // update borders
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
        clusterSum[clusterAssignments[n]][d] += dataPoints[n][d];
      }
    }
  }

  private void updateClusterCenterAssignments() {
    // step 1
    for (int k = 0; k < K; k++) {
      double minDistance = Double.MAX_VALUE;
      for (int l = 0; l < K; l++) {
        var currentDistance = distance.compute(clusterCenters[k], clusterCenters[l]);
        interClusterDistances[k][l] = currentDistance;
        if (k != l && currentDistance < minDistance) {
          minDistance = currentDistance;
        }
      }
      s[k] = 0.5 * minDistance;
    }

    // step 2
    List<Integer> relevantIndices = new ArrayList<>();
    for (int n = 0; n < N; n++) {
      if (upperBounds[n] > s[clusterAssignments[n]]) {
        relevantIndices.add(n);
      }
    }

    // step 3
    for (int k = 0; k < K; k++) {
      for (int n : relevantIndices) {
        if (k != clusterAssignments[n] && upperBounds[n] > lowerBounds[n][k]
            && upperBounds[n] > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
          // step 3a
          double minDistance;
          if (r[n]) {
            minDistance = distance.compute(dataPoints[n], clusterCenters[clusterAssignments[n]]);
            r[n] = false;
          } else {
            minDistance = upperBounds[n];
          }

          // step 3b
          if (minDistance > lowerBounds[n][k] || minDistance > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
            double newDistance = distance.compute(dataPoints[n], clusterCenters[k]);
            if (newDistance < minDistance) {
              clusterAssignments[n] = k;
              upperBounds[n] = minDistance;
            }
          }
        }
      }
    }
  }

  private void updateClusterCentersAndBounds() {
    // step 4
    hasChanged = Util.updateClusterCenters(clusterCenters, newClusterCenters, clusterAssignments, dataPoints);

    // step 5
    for (var lowerBound : lowerBounds) {
      for (int k = 0; k < K; k++) {
        lowerBound[k] = Math.max(lowerBound[k] - distance.compute(clusterCenters[k], newClusterCenters[k]), 0);
      }
    }

    // step 6
    for (int n = 0; n < N; n++) {
      upperBounds[n] += distance.compute(newClusterCenters[clusterAssignments[n]],
          clusterCenters[clusterAssignments[n]]);
    }
    Arrays.fill(r, true);

    // step 7
    clusterCenters = newClusterCenters;
  }
}
