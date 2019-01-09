package kmeans.drake;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

public class JanisDrakeKMeansStrategy extends KMeansStrategy {
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

  private CenterTuple[] centerTuples;

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    this.K = initialClusterCenters.length;
    final int N = dataPoints.length;
    // this.N = N2;

    this.D = dataPoints[0].length;

    this.distance = distance;
    this.hasChanged = true;

    int B = Math.max(K >> 2, 1);
    if (B < 2) {
      B = 2;
    }
    if (K <= B) {
      B = K - 1;
    }

    final int minB = Math.max(K >> 3, 1);

    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);

    this.dataPointsAssignments = new int[N];
    Arrays.fill(dataPointsAssignments, -1);
    this.upperBounds = new double[N];
    final CenterTuple[][] lowerBounds = new CenterTuple[N][B];
    this.centerTuples = new CenterTuple[K];
    for (int c = 0; c < K; c++) {
      centerTuples[c] = new CenterTuple(0, c);
    }

    this.clusterSums = new double[K][D];
    this.clusterSizes = new int[K];
    this.clusterCenterMovements = new double[K];

    for (int n = 0; n < N; n++) {
      sortCenters(n, dataPoints[n], B, centerTuples, clusterCenters, dataPointsAssignments, upperBounds, lowerBounds[n],
          clusterSums, clusterSizes);
    }

    int iterations = 0;
    while (true) {
      int maxB = 0;
      outer: for (int n = 0; n < N; n++) {
        for (int b = 0; b < B; b++) {
          maxB = b > maxB ? b : maxB;
          if (upperBounds[n] < lowerBounds[n][b].distance) {
            if (b == 0) {
              continue outer;
            }
            reorderBounds(n, dataPoints[n], b, clusterCenters, dataPointsAssignments, upperBounds, lowerBounds[n],
                clusterSums, clusterSizes);
            continue outer;
          }
        }
        sortCenters(n, dataPoints[n], B, centerTuples, clusterCenters, dataPointsAssignments, upperBounds,
            lowerBounds[n], clusterSums, clusterSizes);
      }

      double furthestDistanceMoved = Double.NEGATIVE_INFINITY;
      // update centers
      for (int c = 0; c < K; c++) {
        final double[] newCenter = new double[D];
        for (int d = 0; d < clusterSums[c].length; d++) {
          newCenter[d] = clusterSums[c][d] / clusterSizes[c];
        }

        final double dist = distance.compute(newCenter, clusterCenters[c]);

        furthestDistanceMoved = dist > furthestDistanceMoved ? dist : furthestDistanceMoved;

        clusterCenterMovements[c] = dist;
        clusterCenters[c] = newCenter;
      }

      // update bounds
      for (int n = 0; n < N; n++) {
        upperBounds[n] += clusterCenterMovements[dataPointsAssignments[n]];

        lowerBounds[n][B - 1].distance -= furthestDistanceMoved;
        for (int i = B - 2; i >= 0; i--) {
          lowerBounds[n][i].distance -= clusterCenterMovements[lowerBounds[n][i].index];

          if (lowerBounds[n][i].distance > lowerBounds[n][i + 1].distance) {
            lowerBounds[n][i].distance = lowerBounds[n][i + 1].distance;
          }
        }
      }

      if (iterations > 10 && maxB < B) {
        B = Math.max(maxB, minB);
      }

      iterations++;
      if (furthestDistanceMoved == 0) {
        return result();
      }
    }
  }

  private void insertSorted(CenterTuple c, CenterTuple[] arr) {
    for (int i = arr.length - 1; i >= 0; i--) {
      if (arr[i] == null) {
        arr[i] = c;
        break;
      } else if (arr[i].distance < c.distance) {
        // shift all others forward
        CenterTuple prev = arr[i];
        for (int j = i; j >= 1; j--) {
          if (arr[j] == null)
            break;
          CenterTuple tmp = prev;
          prev = arr[j - 1];
          arr[j - 1] = tmp;
        }
        arr[i] = c;
        break;
      }
    }
  }

  void reorderBounds(int i, double[] x, int b, double[][] centers, int[] assignments, double[] upperBounds,
      CenterTuple[] lowerBound, double[][] centerSum, int[] centerSize) {
    final CenterTuple[] relevantTuples = new CenterTuple[b + 1];

    relevantTuples[b] = new CenterTuple(distance.compute(x, centers[assignments[i]]), assignments[i]);
    for (int j = 0; j < b; j++) {
      CenterTuple tup = new CenterTuple(distance.compute(x, centers[lowerBound[j].index]), lowerBound[j].index);

      insertSorted(tup, relevantTuples);
    }

    CenterTuple closest = relevantTuples[0];
    if (assignments[i] != closest.index) {
      removeFromCenter(x, assignments[i], centerSum, centerSize);

      assignments[i] = closest.index;

      addToCenter(x, closest.index, centerSum, centerSize);
    }
    upperBounds[i] = closest.distance;
    for (int j = 0; j < b; j++) {
      lowerBound[j] = relevantTuples[j + 1].copy();
    }
  }

  void sortCenters(int i, double[] x, int b, CenterTuple[] centerTuples, double[][] centers,
      int[] dataPointsAssignments, double[] upperB, CenterTuple[] lowerB, double[][] centerSum, int[] centerSize) {
    final CenterTuple[] sortedTup = new CenterTuple[centers.length];
    for (int j = 0; j < centers.length; j++) {
      double dist = distance.compute(x, centers[j]);

      CenterTuple tup = centerTuples[j];
      tup.distance = dist;
      tup.index = j;

      insertSorted(tup, sortedTup);
    }

    CenterTuple closest = sortedTup[0];
    if (dataPointsAssignments[i] != closest.index) {
      if (dataPointsAssignments[i] != -1)
        removeFromCenter(x, dataPointsAssignments[i], centerSum, centerSize);

      dataPointsAssignments[i] = closest.index;

      addToCenter(x, closest.index, centerSum, centerSize);
    }
    upperB[i] = closest.distance;

    for (int j = 0; j < b; j++) {
      lowerB[j] = sortedTup[j + 1].copy();
    }
  }

  private void removeFromCenter(double[] x, int assignment, double[][] centerSum, int[] centerSize) {
    for (int d = 0; d < centerSum[assignment].length; d++) {
      centerSum[assignment][d] -= x[d];
    }
    centerSize[assignment]--;
  }

  private void addToCenter(double[] x, int assignment, double[][] centerSum, int[] centerSize) {
    for (int d = 0; d < centerSum[assignment].length; d++) {
      centerSum[assignment][d] += x[d];
    }
    centerSize[assignment]++;
  }

  @Override
  protected void initialize() {

  }

  @Override
  protected void loop() {

  }

}
