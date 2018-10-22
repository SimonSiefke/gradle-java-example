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
    // TODO see
    // https://github.com/siddheshk/Faster-Kmeans/blob/master/Code/heuristic_triangleinequality.py

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

    final boolean[] r = new boolean[N];

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
        var currentDistance = Distance.DEFAULT(dataPoints[n], clusters[k].center);
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
      // clusters[closestClusterIndex].closestPoints.add(dataPoints[n]);
      upperBounds[n] = minDistance;
    }

    System.out.println("Upper bounds");
    System.out.println(Arrays.toString(upperBounds));
    System.out.println('\n');

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;
      System.out.println('\n');
      System.out.println("start iteration" + numberOfIterations);
      System.out.println('\n');

      // step 1
      for (int k = 0; k < K; k++) {
        double minDistance = Double.MAX_VALUE;
        for (int l = 0; l < K; l++) {
          var currentDistance = Distance.DEFAULT(clusters[k].center, clusters[l].center);
          interClusterDistances[k][l] = currentDistance;
          if (k != l && currentDistance < minDistance) {
            minDistance = currentDistance;
          }
        }
        sC[k] = 0.5 * minDistance;
      }

      System.out.println("SC");
      System.out.println(Arrays.toString(sC));
      System.out.println('\n');

      // TODO switch: first over n then over k because its so in the paper
      // step 2
      for (int n = 0; n < N; n++) {
        System.out.println("step 2");
        System.out.println(upperBounds[n] <= sC[clusterAssignments[n]]);
        if (upperBounds[n] <= sC[clusterAssignments[n]]) {
          continue;
        }

        System.out.println("step 2 n " + n);

        // step 3
        for (int k = 0; k < K; k++) {
          System.out.println(Arrays.toString(clusters[k].center));
          System.out.println(Distance.DEFAULT(dataPoints[n], clusters[clusterAssignments[n]].center));
          System.out.println('\n');
          System.out.println("ITERATION: k= " + k + "  n= " + n);
          if (k != clusterAssignments[n] && upperBounds[n] > lowerBounds[n][k]
              && upperBounds[n] > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
            System.out.println("step 3a");
            // step 3a
            double minDistance;
            if (r[n]) {
              System.out.println("no upper bound");
              minDistance = Distance.DEFAULT(dataPoints[n], clusters[clusterAssignments[n]].center);
              System.out.println("point " + Arrays.toString(dataPoints[n]) + " is assigned to cluster "
                  + Arrays.toString(clusters[clusterAssignments[n]].center));
              System.out.println("current distance " + minDistance);
              r[n] = false;
            } else {
              System.out.println("upper bound");
              minDistance = upperBounds[n];
            }

            // step 3b
            if (minDistance > lowerBounds[n][k]
                || minDistance > 0.5 * interClusterDistances[clusterAssignments[n]][k]) {
              double newDistance = Distance.DEFAULT(dataPoints[n], clusters[k].center);
              System.out.println("step 3b");
              if (newDistance < minDistance) {
                System.out.println("current distance" + minDistance);
                System.out.println("new distance" + newDistance);
                System.out.println(Arrays.toString(dataPoints[n]) + " to " + Arrays.toString(clusters[k].center));
                clusterAssignments[n] = k;
                upperBounds[n] = minDistance;
                // clusters[k].closestPoints.add(dataPoints[n]);
              }
            }
          }
        }
      }

      // for (var c : clusters) {
      // System.out.println(Arrays.toString(c.closestPoints.toArray()));
      // System.out.println(Util.averageOfPoints(c.closestPoints));
      // }

      // step 4
      for (var cluster : clusters) {

        cluster.closestPoints.clear();
      }
      for (var n = 0; n < N; n++) {
        clusters[clusterAssignments[n]].closestPoints.add(dataPoints[n]);
      }
      for (var cluster : clusters) {
        System.out.println(Arrays.toString(cluster.center));
        System.out.println(Arrays.deepToString(cluster.closestPoints.toArray()));
        System.out.println('\n');
      }

      var newClusterCenters = new double[K][];
      for (int k = 0; k < K; k++) {
        if (clusters[k].closestPoints.size() == 0) {
          newClusterCenters[k] = clusters[k].center;
          continue;
        }
        var currentCenter = clusters[k].center;

        var newCenter = Util.averageOfPoints(clusters[k].closestPoints);
        newClusterCenters[k] = newCenter;
        if (Distance.DEFAULT(currentCenter, newCenter) > 0) {
          hasChanged = true;
        }
      }

      // double[][] newClusterCenters = Arrays.stream(clusters).map(cluster ->
      // Util.averageOfPoints(cluster.closestPoints))
      // .toArray(double[][]::new);

      // step 5
      for (var lowerBound : lowerBounds) {
        for (int k = 0; k < K; k++) {
          // System.out.println(k);
          System.out.println(newClusterCenters[k]);
          // System.out.println(clusters[k].center);
          lowerBound[k] = Math.max(lowerBound[k] - Distance.DEFAULT(clusters[k].center, newClusterCenters[k]), 0);
        }
      }

      System.out.println('\n');
      System.out.println("before step 6 upper bounds");
      System.out.println(Arrays.toString(upperBounds));
      System.out.println('\n');

      // step 6
      for (int n = 0; n < N; n++) {
        upperBounds[n] += Distance.DEFAULT(newClusterCenters[clusterAssignments[n]],
            clusters[clusterAssignments[n]].center);
        r[n] = true;
      }

      System.out.println('\n');
      System.out.println("step 6 upper bounds");
      System.out.println(Arrays.toString(upperBounds));
      System.out.println('\n');

      // step 7
      for (int k = 0; k < K; k++) {
        var currentCenter = clusters[k].center;
        var newCenter = newClusterCenters[k];
        clusters[k].center = newCenter;
        System.out.println("step 7");
        System.out.println(Arrays.toString(clusters[k].center));
        System.out.println(Arrays.toString(newClusterCenters[k]));
        System.out.println('\n');
        if (Distance.DEFAULT(currentCenter, newCenter) > 0) {
          hasChanged = true;
          System.out.println("change elkan");
        }
      }

      System.out.println("haschange" + hasChanged);
      if (!hasChanged) {
        System.out.println("elkan stops after " + numberOfIterations + " iterations");
      }

      System.out.println('\n');
      System.out.println("end iteration " + numberOfIterations);
      System.out.println('\n');
      System.out.println('\n');
      System.out.println('\n');
      System.out.println('\n');
      numberOfIterations++;
    }
    return clusters;
  }
}
