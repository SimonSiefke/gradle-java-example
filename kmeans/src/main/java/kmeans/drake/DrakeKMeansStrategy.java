package kmeans.drake;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Drake's KMeans Strategy.
 */
public class DrakeKMeansStrategy extends KMeansStrategy {
  /**
   * stores for each point how far away the second closest center is.
   */
  private double[][] lowerBounds;
  /**
   * stores for each center how far it has moved in the current iteration.
   */
  private double[] clusterCentersDistanceMoved;
  /**
   * stores for each point how far away its closest center maximally is.
   */
  private double[] upperBounds;

  /**
   * stores the number of bounds (changes over time).
   */
  private int B;

  /**
   * stores the minimal number of bounds.
   */
  private int minB;

  /**
   * stores the maximal number of bounds.
   */
  private int maxB;

  /**
   * stores for each lower bound to which cluster center it is assigned.
   */
  private int[][] lowerBoundsAssignments;

  /**
   * stores the first cluster center.
   */
  private double[] firstClusterCenter;

  /**
   * stores the index of the center that has moved the most in the current
   * iteration.
   */
  private double maxDistanceMoved;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;
    this.B = computeInitialB();
    this.minB = computeInitialMinB();

    this.dataPointsAssignments = new int[N];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterCentersDistanceMoved = new double[K];
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.distance = distance;
    this.lowerBounds = new double[N][B];
    this.lowerBoundsAssignments = new int[N][B];
    this.maxDistanceMoved = 0;
    this.upperBounds = new double[N];

    this.hasChanged = true;
    this.numberOfIterations = 0;

    initialize();
    main();
    return result();
  }

  protected void updateBounds() {
    for (int n = 0; n < N; n++) {
      upperBounds[n] += clusterCentersDistanceMoved[dataPointsAssignments[n]];
      lowerBounds[n][B - 1] -= maxDistanceMoved;
      for (int b = B - 2; b >= 0; b--) {
        lowerBounds[n][B - 1] -= clusterCentersDistanceMoved[lowerBoundsAssignments[n][b]];
        if (lowerBounds[n][b] > lowerBounds[n][b + 1]) {
          lowerBounds[n][b] = lowerBounds[n][b + 1];
        }
      }
    }
  }

  private int computeInitialB() {
    return 1;
    // var result = K / 4;
    // if (result < 2) {
    // result = 2;
    // }
    // if (K <= result) {
    // result = K - 1;
    // }
    // return result;
  }

  private int computeInitialMinB() {
    return Math.max(K / 8, 1);
  }

  @Override
  protected void initialize() {
    for (int n = 0; n < N; n++) {
      sortCenters(n, B - 1, clusterCenters);
      dataPointsAssignments[n] = lowerBoundsAssignments[n][0];
      clusterSizes[dataPointsAssignments[n]]++;
      for (int d = 0; d < D; d++) {
        clusterSums[dataPointsAssignments[n]][d] += dataPoints[n][d];
      }
    }
  }

  private void sortCenters(int n, int numberOfBounds, double[][] clusterCenters) {
    var dataPoint = dataPoints[n];
    Arrays.sort(clusterCenters,
        (a, b) -> this.distance.compute(a, dataPoint) > this.distance.compute(b, dataPoint) ? 1 : -1);
    firstClusterCenter = clusterCenters[0];
    upperBounds[n] = this.distance.compute(dataPoint, firstClusterCenter);
    for (int b = 0; b < numberOfBounds; b++) {
      lowerBoundsAssignments[n][b] = b + 1;
      lowerBounds[n][b] = this.distance.compute(dataPoint, clusterCenters[lowerBoundsAssignments[n][b]]);
    }
  }

  @Override
  protected void loop() {
    maxB = 0;
    outerLoop: for (int n = 0; n < N; n++) {
      for (int b = 0; b < B; b++) {
        maxB = Math.max(b, maxB);
        if (upperBounds[n] <= lowerBounds[n][b]) {
          var r = new double[b + 1][];
          r[0] = firstClusterCenter;
          for (int o = 1; o < r.length; o++) {
            r[o] = clusterCenters[lowerBoundsAssignments[n][o]];
          }
          sortCenters(n, b, r);
          continue outerLoop;
        }
      }
      sortCenters(n, B - 1, clusterCenters);
    }

    int furthestMovingIndex = moveCenters();
    maxDistanceMoved = clusterCenterMovements[furthestMovingIndex];

    updateBounds();

    if (numberOfIterations > 10 && maxB < B) {
      B = Math.max(maxB, minB);
    }
  }

}
