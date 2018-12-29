package kmeans.elkan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Elkan's KMeans Strategy.
 */
public class ElkanKMeansStrategy extends KMeansStrategy {
  private double[][] interClusterDistances;
  private double[][] lowerBounds;
  private double[][] newClusterCenters; // used in step 4
  private boolean[] r;
  private double[] s;
  private double[] upperBounds;
  /**
   * stores for each center how far it has moved in the current iteration.
   */
  private double[] clusterCentersDistanceMoved;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    // TODO see
    // https://www.programcreek.com/java-api-examples/index.php?source_dir=JSAT-master/JSAT/src/jsat/clustering/kmeans/ElkanKernelKMeans.java
    // TODO see
    // https://github.com/siddheshk/Faster-Kmeans/blob/master/Code/heuristic_triangleinequality.py

    // TODO javadoc for methods
    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.dataPointsAssignments = new int[N];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;

    this.interClusterDistances = new double[K][K];
    this.clusterCentersDistanceMoved = new double[K];
    this.lowerBounds = new double[N][K];
    this.newClusterCenters = new double[K][D];
    this.r = new boolean[N];
    this.s = new double[K];
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

  // TODO make this easier
  private void initialize() {
    // step -1: assign each point to its nearest cluster using Lemma 1
    final boolean[] skip = new boolean[K];
    for (int n = 0; n < N; n++) {
      double minDistance = Double.MAX_VALUE;
      int closestClusterIndex = -1;
      Arrays.fill(skip, false);
      for (int k = 0; k < K; k++) {
        if (skip[k]) {
          continue;
        }
        var currentDistance = distance.compute(dataPoints[n], clusterCenters[k]);
        lowerBounds[n][k] = currentDistance;
        if (currentDistance < minDistance) {
          minDistance = currentDistance;
          closestClusterIndex = k;
          // use lemma 1 to see if some iterations can be skipped
          for (int l = k + 1; l < K; l++) {
            if (interClusterDistances[k][l] >= 2 * currentDistance) {
              skip[l] = true;
            }
          }
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
        interClusterDistances[k][l] = currentDistance;
        if (k != l && currentDistance < minDistance) {
          minDistance = currentDistance;
        }
      }
      s[k] = 0.5 * minDistance;
    }

    // step 2
    // TODO check if step 2 is useful
    List<Integer> relevantIndices = new ArrayList<>();
    for (int n = 0; n < N; n++) {
      if (upperBounds[n] > s[dataPointsAssignments[n]]) {
        relevantIndices.add(n);
      }
    }

    // step 3
    for (int k = 0; k < K; k++) {
      for (int n : relevantIndices) {
        if (k != dataPointsAssignments[n] && upperBounds[n] > lowerBounds[n][k]
            && upperBounds[n] > 0.5 * interClusterDistances[dataPointsAssignments[n]][k]) {
          // step 3a
          if (r[n]) {
            upperBounds[n] = distance.compute(dataPoints[n], clusterCenters[dataPointsAssignments[n]]);
            lowerBounds[n][dataPointsAssignments[n]] = upperBounds[n];
            r[n] = false;
          }

          // step 3b
          if (upperBounds[n] > lowerBounds[n][k]
              || upperBounds[n] > 0.5 * interClusterDistances[dataPointsAssignments[n]][k]) {
            lowerBounds[n][k] = distance.compute(dataPoints[n], clusterCenters[k]);
            if (lowerBounds[n][k] < upperBounds[n]) {
              upperBounds[n] = lowerBounds[n][k];
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
          newClusterCenters[k][d] = newClusterCenterCoordinate;
        }
        clusterCentersDistanceMoved[k] = distance.compute(clusterCenters[k], newClusterCenters[k]);
      } else {
        throw new IllegalArgumentException(
            "Please provide different initial cluster centers, one or more of your initial clusters are too far away from any data point");
      }
    }

    // step 5
    for (var lowerBound : lowerBounds) {
      for (int k = 0; k < K; k++) {
        lowerBound[k] = Math.max(lowerBound[k] - clusterCentersDistanceMoved[k], 0);
      }
    }

    // step 6
    for (int n = 0; n < N; n++) {
      upperBounds[n] += clusterCentersDistanceMoved[dataPointsAssignments[n]];
    }
    Arrays.fill(r, true);

    // step 7
    // TODO deep copy
    clusterCenters = newClusterCenters;
  }
}
