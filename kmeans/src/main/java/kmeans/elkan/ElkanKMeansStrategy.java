package kmeans.elkan;

import java.util.Arrays;

import javax.annotation.Nonnull;

import distance.Distance;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Elkan KMeans Strategy.
 */
public class ElkanKMeansStrategy implements KMeansStrategy {
  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations) {
    // TODO see
    // https://www.programcreek.com/java-api-examples/index.php?source_dir=JSAT-master/JSAT/src/jsat/clustering/kmeans/ElkanKernelKMeans.java

    // TODO indices naming strategy:
    // N = # data points
    // n or o or p = data point index
    // K = # Clusters
    // k or l or m = cluster index
    // D = Dimension
    final int N = dataPoints.length;
    final int K = initialClusterCenters.length;

    final int[] clusterAssignments = new int[N]; // maps data point indices to cluster indices
    final double[][] lowerBounds = new double[N][K];
    final double[] upperBounds = new double[N];

    final double[][] interClusterDistances = new double[K][K];
    final double[] sC = new double[K];

    int numberOfIterations = 0;
    boolean hasChanged = true;
    final Cluster[] clusters = Arrays.stream(initialClusterCenters).map(Cluster::new).toArray(Cluster[]::new);

    // TODO compute interclusterdistances?

    // step -2: set all lower bounds to 0
    for (var lowerBound : lowerBounds) {
      Arrays.fill(lowerBound, 0);
    }

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
        var currentDistance = Distance.EUCLIDEAN(dataPoints[n], clusters[k].center);
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
      clusters[closestClusterIndex].closestPoints.add(dataPoints[n]);
      upperBounds[n] = minDistance;
    }

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // step 1
      for (int k = 0; k < K; k++) {
        double minDistance = Double.MAX_VALUE;
        for (int l = 0; l < K; l++) {
          var currentDistance = Distance.EUCLIDEAN(clusters[k].center, clusters[l].center);
          interClusterDistances[k][l] = currentDistance;
          if (k != l && currentDistance < minDistance) {
            minDistance = currentDistance;
          }
        }
        sC[k] = 0.5 * minDistance;
      }

      final boolean[] r = new boolean[N];

      // step 2
      for (int n = 0; n < N; n++) {
        if (upperBounds[n] <= sC[clusterAssignments[n]]) {
          continue;
        }
        // step 3
        for (int k = 0; k < K; k++) {
          if (k != clusterAssignments[n] && upperBounds[n] > lowerBounds[n][k]
              && upperBounds[n] > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
            // step 3a
            double currentDistance;
            if (r[n]) {
              currentDistance = Distance.EUCLIDEAN(dataPoints[n], clusters[clusterAssignments[n]].center);
              r[n] = false;
            } else {
              currentDistance = upperBounds[n];
            }

            // step 3b
            if (currentDistance > lowerBounds[n][k]
                || currentDistance > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
              double newDistance = Distance.EUCLIDEAN(dataPoints[n], clusters[k].center);
              if (newDistance < currentDistance) {
                clusterAssignments[n] = k;
              }
            }
          }
        }
      }

      // step 4
      double[][] newClusterCenters = Arrays.stream(clusters).map(cluster -> Util.averageOfPoints(cluster.closestPoints))
          .toArray(double[][]::new);

      // step 5
      for (var lowerBound : lowerBounds) {
        for (int k = 0; k < K; k++) {
          lowerBound[k] = Math.max(lowerBound[k] - Distance.EUCLIDEAN(clusters[k].center, newClusterCenters[k]), 0);
        }
      }

      // step 6
      for (int n = 0; n < N; n++) {
        upperBounds[n] += Distance.EUCLIDEAN(newClusterCenters[clusterAssignments[n]],
            clusters[clusterAssignments[n]].center);
        r[n] = true;
      }

      // step 7
      for (int k = 0; k < K; k++) {
        var currentCenter = clusters[k].center;
        var newCenter = newClusterCenters[k];
        clusters[k].center = newCenter;
        if (Distance.EUCLIDEAN(currentCenter, newCenter) > 0) {
          hasChanged = true;
          System.out.println("change elkan");
        }
      }

      numberOfIterations++;
    }
    return clusters;
  }
}
