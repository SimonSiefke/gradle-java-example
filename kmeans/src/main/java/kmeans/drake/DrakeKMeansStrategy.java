package kmeans.drake;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Drake's KMeans Strategy.
 */
public class DrakeKMeansStrategy extends KMeansStrategy {
  /**
   * stores for each point how far away the second closest center is.
   */
  private double[] lowerBounds;
  /**
   * stores for each center how far it has moved in the current iteration.
   */
  private double[] clusterCentersDistanceMoved;
  /**
   * stores for each point how far away its closest center maximally is.
   */
  private double[] upperBounds;

  /**
   * stores the number of bounds (changes over time)
   */
  private int B;

  /**
   * stores the minimal number of bounds
   */
  private int minB;

  /**
   * stores for each lower bound to which cluster center it is assigned.
   */
  private int[][] lowerBoundAssignments;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;
    this.B = computeInitialB();
    this.minB = computeInitialMinB();

    this.dataPointAssignments = new int[N];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.lowerBounds = new double[N];
    this.clusterCentersDistanceMoved = new double[K];
    this.upperBounds = new double[N];

    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

      // outerLoop: for (int n = 0; n < N; n++) {
      // for (int b = 0; b < B; b++) {
      // if (upperBounds[n] <= lowerBounds[n][b]) {
      // continue outerLoop;
      // }
      // }
      // }

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
      // double mostDistanceMoved = Double.MIN_VALUE;
      // for (int k = 0; k < K; k++) {
      // mostDistanceMoved = Math.max(mostDistanceMoved,
      // clusterCentersDistanceMoved[k]);
      // }
      // for(int n=0;n<N;n++){
      // upperBounds[n]+=clusterCentersDistanceMoved[first];
      // lowerBounds[n][B-1]-=mostDistanceMoved;
      // for(int b=B-2;b>=0;b--){
      // lowerBounds[n][b]-=clusterCentersDistanceMoved[assignments2[n,b]];
      // if(lowerBounds[n][b] > lowerBounds[n][b+1]){
      // lowerBounds[n][b] = lowerBounds[n][b+1];
      // }
      // }
      // }

      numberOfIterations++;
    }

    final Cluster[] clusters = Arrays.stream(clusterCenters).map(Cluster::new).toArray(Cluster[]::new);
    for (int n = 0; n < N; n++) {
      clusters[dataPointAssignments[n]].closestPoints.add(dataPoints[n]);
    }
    return clusters;
  }

  private int computeInitialB() {
    var result = K / 4;
    if (result < 2) {
      result = 2;
    }
    if (this.K <= result) {
      result = K - 1;
    }
    return result;
  }

  private int computeInitialMinB() {
    return Math.max(K / 8, 1);
  }

  private void initialize() {

  }

  private void sortCenters(int n) {
    var dataPoint = dataPoints[n];
    // sort r by increasing distance from x[i]
    Arrays.sort(clusterCenters,
        (a, b) -> this.distance.compute(a, dataPoint) > this.distance.compute(b, dataPoint) ? 1 : -1);
    var first = clusterCenters[0];
    upperBounds[n] = this.distance.compute(dataPoint, first);
    for (int b = 0; b < B; b++) {
      // clusterAssignments[n][b] = clusterCenters[b+1];
      // lowerBounds[n][b] = this.distance.compute(dataPoint,
      // clusterAssignments[n][b]);
      // clusterAssignments
    }
  }
}
