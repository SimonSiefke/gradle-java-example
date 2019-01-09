package kmeans.drake;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

public class JanisDrakeKMeansStrategy extends KMeansStrategy {
  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    this.K = initialClusterCenters.length;
    final int N2 = dataPoints.length;

    this.D = dataPoints[0].length;

    this.distance = distance;

    int B = Math.max(K >> 2, 1);
    if (B < 2)
      B = 2;
    if (K <= B)
      B = K - 1;

    final int minB = Math.max(K >> 3, 1);

    final double[][] centers = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    // TODO change to input.hasPresetCenters to avoid the deep copy here

    final int[] assignments = new int[N2];
    Arrays.fill(assignments, -1);
    final double[] upperBounds = new double[N2];
    final CenterTuple[][] lowerBounds = new CenterTuple[N2][B];
    final CenterTuple[] centerTuples = new CenterTuple[K];
    for (int c = 0; c < K; c++) {
      centerTuples[c] = new CenterTuple(0, c);
    }

    final double[][] centerSum = new double[K][D];
    final int[] centerSize = new int[K];

    long distanceCalcs = 0;

    for (int n = 0; n < N2; n++) {
      sortCenters(n, dataPoints[n], B, centerTuples, centers, assignments, upperBounds, lowerBounds[n], centerSum,
          centerSize);
      distanceCalcs += K;
    }

    int iterations = 0;
    while (true) {
      int maxB = 0;
      outer: for (int n = 0; n < N2; n++) {
        for (int b = 0; b < B; b++) {
          maxB = b > maxB ? b : maxB;
          if (upperBounds[n] < lowerBounds[n][b].distance) {
            if (b == 0)
              continue outer;
            reorderBounds(n, dataPoints[n], b, centers, assignments, upperBounds, lowerBounds[n], centerSum,
                centerSize);
            distanceCalcs += b;
            continue outer;
          }
        }
        sortCenters(n, dataPoints[n], B, centerTuples, centers, assignments, upperBounds, lowerBounds[n], centerSum,
            centerSize);
        distanceCalcs += K;
      }

      final double[] centerMoves = new double[K];
      double maxMoved = Double.NEGATIVE_INFINITY;
      // update centers
      for (int c = 0; c < K; c++) {
        final double[] newCenter = new double[D];
        for (int d = 0; d < centerSum[c].length; d++) {
          newCenter[d] = centerSum[c][d] / centerSize[c];
        }

        final double dist = distance.compute(newCenter, centers[c]);

        maxMoved = dist > maxMoved ? dist : maxMoved;

        centerMoves[c] = dist;
        centers[c] = newCenter;
      }
      distanceCalcs += K;

      // update bounds
      for (int n = 0; n < N2; n++) {
        upperBounds[n] += centerMoves[assignments[n]];

        lowerBounds[n][B - 1].distance -= maxMoved;
        for (int i = B - 2; i >= 0; i--) {
          lowerBounds[n][i].distance -= centerMoves[lowerBounds[n][i].index];

          if (lowerBounds[n][i].distance > lowerBounds[n][i + 1].distance) {
            lowerBounds[n][i].distance = lowerBounds[n][i + 1].distance;
          }
        }
      }

      if (iterations > 10 && maxB < B) {
        B = Math.max(maxB, minB);
      }

      iterations++;
      if (maxMoved == 0) {
        this.clusterCenters = centers;
        this.dataPointsAssignments = assignments;
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

  void sortCenters(int i, double[] x, int b, CenterTuple[] centerTuples, double[][] centers, int[] assignments,
      double[] upperB, CenterTuple[] lowerB, double[][] centerSum, int[] centerSize) {
    final CenterTuple[] sortedTup = new CenterTuple[centers.length];
    for (int j = 0; j < centers.length; j++) {
      System.out.println(Arrays.toString(x));
      System.out.println(Arrays.toString(centers[j]));
      System.out.println("ok before");
      System.out.println(distance);
      System.out.println(distance.compute(new double[] { 0.0 }, new double[] { 0.0 }));
      System.out.println("ok middle");
      double dist = distance.compute(x, centers[j]);
      System.out.println("ok after");

      CenterTuple tup = centerTuples[j];
      tup.distance = dist;
      tup.index = j;

      insertSorted(tup, sortedTup);
    }

    CenterTuple closest = sortedTup[0];
    if (assignments[i] != closest.index) {
      if (assignments[i] != -1)
        removeFromCenter(x, assignments[i], centerSum, centerSize);

      assignments[i] = closest.index;

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
