package kmeans.elkan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Elkan's KMeans Strategy.
 */
public class ElkanKMeansStrategy extends KMeansStrategy {
  /**
   * stores for each cluster the distance to every other cluster
   */
  private double[][] interClusterDistances;
  /**
   * stores for each point how least far away each the cluster center is.
   */
  private double[][] lowerBounds;
  /**
   * stores for each point whether the upper bound to its currently assigned
   * center is accurate or not
   */
  private boolean[] hasAccurateUpperBound;
  /**
   * TODO
   */
  private double[] s;
  /**
   * stores for each point how far away its closest center maximally is.
   */
  private double[] upperBounds;

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
    this.clusterCenterMovements = new double[K];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.hasChanged = true;
    this.interClusterDistances = new double[K][K];
    this.lowerBounds = new double[N][K];
    this.maxNumberOfIterations = maxNumberOfIterations;
    this.numberOfIterations = 0;
    this.hasAccurateUpperBound = new boolean[N];
    this.s = new double[K];
    this.upperBounds = new double[N];

    initialize();
    main();
    return result();
  }

  @Override
  protected void initialize() {
    for (int n = 0; n < N; n++) {
      double minDistance = Double.MAX_VALUE;
      int minDistanceIndex = -1;
      for (int k = 0; k < K; k++) {
        var currentDistance = distance.compute(dataPoints[n], clusterCenters[k]);
        lowerBounds[n][k] = currentDistance;
        if (currentDistance < minDistance) {
          minDistance = currentDistance;
          minDistanceIndex = k;
        }
      }
      upperBounds[n] = minDistance;
      initialAssignPointToCluster(n, minDistanceIndex);
    }
  }

  private void updateAssignments() {
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
          if (hasAccurateUpperBound[n]) {
            upperBounds[n] = distance.compute(dataPoints[n], clusterCenters[dataPointsAssignments[n]]);
            lowerBounds[n][dataPointsAssignments[n]] = upperBounds[n];
            hasAccurateUpperBound[n] = false;
          }

          // step 3b
          if (upperBounds[n] > lowerBounds[n][k]
              || upperBounds[n] > 0.5 * interClusterDistances[dataPointsAssignments[n]][k]) {
            lowerBounds[n][k] = distance.compute(dataPoints[n], clusterCenters[k]);
            if (lowerBounds[n][k] < upperBounds[n]) {
              upperBounds[n] = lowerBounds[n][k];
              assignPointToCluster(n, k);
            }
          }
        }
      }
    }
  }

  @Override
  protected void updateBounds() {
    // step 5
    for (var lowerBound : lowerBounds) {
      for (int k = 0; k < K; k++) {
        lowerBound[k] = Math.max(lowerBound[k] - clusterCenterMovements[k], 0);
      }
    }

    // step 6
    for (int n = 0; n < N; n++) {
      upperBounds[n] += clusterCenterMovements[dataPointsAssignments[n]];
    }
    Arrays.fill(hasAccurateUpperBound, true);
  }

  @Override
  protected void loop() {
    updateAssignments();
    moveCenters();
    updateBounds();
  }
}
