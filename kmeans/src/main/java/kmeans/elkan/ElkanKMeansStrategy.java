package kmeans.elkan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Elkan KMeans Strategy.
 */
public class ElkanKMeansStrategy implements KMeansStrategy {
  private DistanceStrategy distance;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    this.distance = distance;
    // TODO see
    // https://www.programcreek.com/java-api-examples/index.php?source_dir=JSAT-master/JSAT/src/jsat/clustering/kmeans/ElkanKernelKMeans.java
    // TODO see
    // https://github.com/siddheshk/Faster-Kmeans/blob/master/Code/heuristic_triangleinequality.py

    final int N = dataPoints.length;
    final int K = initialClusterCenters.length;
    final int D = dataPoints[0].length;

    final int[] clusterAssignments = new int[N]; // maps data point indices to cluster indices
    final double[][] lowerBounds = new double[N][K];
    final double[] upperBounds = new double[N];
    final double[][] interClusterDistances = new double[K][K];
    final double[] s = new double[K];
    final boolean[] r = new boolean[N];
    final Cluster[] clusters = Arrays.stream(initialClusterCenters).map(Cluster::new).toArray(Cluster[]::new);

    int numberOfIterations = 0;
    boolean hasChanged = true;

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
        var currentDistance = this.distance.compute(dataPoints[n], clusters[k].center);
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
      clusterAssignments[n] = closestClusterIndex;
      upperBounds[n] = minDistance;
    }

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // step 1
      for (int k = 0; k < K; k++) {
        double minDistance = Double.MAX_VALUE;
        for (int l = 0; l < K; l++) {
          var currentDistance = this.distance.compute(clusters[k].center, clusters[l].center);
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
          // step 3
          if (k != clusterAssignments[n] && upperBounds[n] > lowerBounds[n][k]
              && upperBounds[n] > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
            // step 3a
            double minDistance;
            if (r[n]) {
              minDistance = this.distance.compute(dataPoints[n], clusters[clusterAssignments[n]].center);
              r[n] = false;
            } else {
              minDistance = upperBounds[n];
            }

            // step 3b
            if (minDistance > lowerBounds[n][k]
                || minDistance > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
              double newDistance = this.distance.compute(dataPoints[n], clusters[k].center);
              if (newDistance < minDistance) {
                clusterAssignments[n] = k;
                upperBounds[n] = minDistance;
              }
            }
          }
        }
      }

      // step 4
      for (var cluster : clusters) {
        cluster.closestPoints.clear();
      }
      for (var n = 0; n < N; n++) {
        clusters[clusterAssignments[n]].closestPoints.add(dataPoints[n]);
      }
      var newClusterCenters = new double[K][];
      for (int k = 0; k < K; k++) {
        if (clusters[k].closestPoints.size() == 0) {
          newClusterCenters[k] = clusters[k].center;
        } else {
          newClusterCenters[k] = Util.averageOfPoints(clusters[k].closestPoints);
        }
      }

      // step 5
      for (var lowerBound : lowerBounds) {
        for (int k = 0; k < K; k++) {
          var difference = this.distance.compute(clusters[k].center, newClusterCenters[k]);
          if (difference > 0) {
            hasChanged = true;
          }
          lowerBound[k] = Math.max(lowerBound[k] - difference, 0);
        }
      }

      // step 6
      for (int n = 0; n < N; n++) {
        upperBounds[n] += this.distance.compute(newClusterCenters[clusterAssignments[n]],
            clusters[clusterAssignments[n]].center);
      }
      Arrays.fill(r, true);

      // step 7
      for (int k = 0; k < K; k++) {
        clusters[k].center = newClusterCenters[k];
      }
      numberOfIterations++;
    }
    return clusters;
  }
}
