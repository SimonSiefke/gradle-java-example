package kmeans.drake;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Drake's KMeans Strategy.
 */
public class DrakeKMeansStrategy extends KMeansStrategy {
  /**
   * stores (in sorted order) for each center its index and its distance.
   */
  private OrderedClusterCenter[] centerOrder;
  /**
   * stores for each point how far away the second closest center is.
   */
  private double[][] lowerBounds;
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
  private double[] closestClusterCenter;

  /**
   * stores the furthest distance that a center that has moved the most in the
   * current iteration.
   */
  private double furthestDistanceMoved;
  /**
   * stores for each data point the indices of the b closest other cluster centers
   * (that the point is not assigned to).
   */
  private int[][] closestOtherCenters;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {

    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;
    this.B = computeInitialB();
    this.minB = computeInitialMinB();

    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterCenterMovements = new double[K];
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.closestOtherCenters = new int[N][B];
    this.centerOrder = new OrderedClusterCenter[K];
    this.dataPoints = dataPoints;
    this.dataPointsAssignments = new int[N];
    this.distance = distance;
    this.hasChanged = true;
    this.lowerBounds = new double[N][B];
    this.lowerBoundsAssignments = new int[N][B];
    this.furthestDistanceMoved = 0;
    this.maxNumberOfIterations = maxNumberOfIterations;
    this.numberOfIterations = 0;
    this.upperBounds = new double[N];

    initialize();
    main();
    return result();
  }

  protected void updateBounds() {
    for (int n = 0; n < N; n++) {
      upperBounds[n] += clusterCenterMovements[dataPointsAssignments[n]];
      lowerBounds[n][B - 1] -= furthestDistanceMoved;
      for (int b = B - 2; b >= 0; b--) {
        lowerBounds[n][B - 1] -= clusterCenterMovements[lowerBoundsAssignments[n][b]];
        if (lowerBounds[n][b] > lowerBounds[n][b + 1]) {
          lowerBounds[n][b] = lowerBounds[n][b + 1];
        }
      }
    }
  }

  private int computeInitialB() {
    var result = K / 4;
    if (result < 2) {
      result = 2;
    }
    if (K <= result) {
      result = K - 1;
    }
    return result;
  }

  private int computeInitialMinB() {
    return Math.max(K / 8, 1);
  }

  @Override
  protected void initialize() {
    for (int n = 0; n < N; n++) {
      int minDistanceIndex = -1;
      var minDistance = Double.MAX_VALUE;
      for (int k = 0; k < K; k++) {
        var currentDistance = distance.compute(dataPoints[n], clusterCenters[k]);
        if (currentDistance < minDistance) {
          minDistance = currentDistance;
          minDistanceIndex = k;
        }
      }
      upperBounds[n] = minDistance;
      initialAssignPointToCluster(n, minDistanceIndex);
    }
  }

  /**
  *
  */
  private void sortCenters(int n, int numberOfLowerBoundsRemaining, double[][] clusterCenters) {
    var dataPoint = dataPoints[n];
    // sort all center by decreasing distance from the data point
    for (int k = 0; k < K; k++) {
      centerOrder[k] = new OrderedClusterCenter(distance.compute(dataPoint, clusterCenters[k]), k);
    }
    // TODO partial sort
    Arrays.sort(centerOrder);

    // assign point to closest center
    assignPointToCluster(n, centerOrder[0].index);

    // set upperbound to the exact distance to the closest center
    upperBounds[n] = centerOrder[0].distance;

    // we know that the first numberOfLowerBoundsRemaining lower bounds are
    // inaccurate so we need to update them
    for (int b = 0; b < numberOfLowerBoundsRemaining; b++) {
      closestOtherCenters[n][b] = centerOrder[b + 1].index;
      lowerBounds[n][b] = centerOrder[b + 1].distance;
    }
  }

  @Override
  protected void loop() {
    maxB = 0;
    outerLoop: for (int n = 0; n < N; n++) {
      for (int b = 0; b < B; b++) {
        maxB = Math.max(b, maxB);
        if (upperBounds[n] <= lowerBounds[n][b]) {
          if (b == 0) {
            continue outerLoop;
          }
          var r = new double[b + 1][];
          r[0] = clusterCenters[0];
          for (int s = 1; s < r.length; s++) {
            r[s] = clusterCenters[lowerBoundsAssignments[n][s]];
          }
          sortCenters(n, b, r);
          continue outerLoop;
        }
      }
      sortCenters(n, B - 1, clusterCenters);
    }

    int furthestMovingIndex = moveCenters();
    furthestDistanceMoved = clusterCenterMovements[furthestMovingIndex];

    updateBounds();

    if (numberOfIterations > 10 && maxB < B) {
      B = Math.max(maxB, minB);
    }
  }
}
