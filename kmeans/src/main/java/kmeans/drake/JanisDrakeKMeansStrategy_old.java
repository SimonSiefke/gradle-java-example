// package kmeans.drake;

// import java.util.AbstractMap;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Map;

// import distance.DistanceStrategy;
// import kmeans.Cluster;
// import kmeans.KMeansStrategy;

// /**
// * Drake's KMeans Strategy.
// */
// public class JanisDrakeKMeansStrategy extends KMeansStrategy {
// /**
// * stores (in sorted order) for each center its index and its distance.
// */
// private OrderedClusterCenter[] centerOrder;
// /**
// * stores for each point how far away the second closest center is.
// */
// private double[][] lowerBounds;
// /**
// * stores for each point how far away its closest center maximally is.
// */
// private double[] upperBounds;

// /**
// * stores the number of bounds (changes over time).
// */
// private int B;

// /**
// * stores the minimal number of bounds.
// */
// private int minB;

// /**
// * stores the maximal number of bounds.
// */
// private int maxB;

// /**
// * stores for each lower bound to which cluster center it is assigned.
// */
// private int[][] lowerBoundsAssignments;

// /**
// * stores the first cluster center.
// */
// private double[] closestClusterCenter;

// /**
// * stores the furthest distance that a center that has moved the most in the
// * current iteration.
// */
// private double furthestDistanceMoved;
// /**
// * stores for each data point the indices of the b closest other cluster
// centers
// * (that the point is not assigned to).
// */
// private int[][] closestOtherCenters;

// @Override
// public Cluster[] cluster(double[][] dataPoints, double[][]
// initialClusterCenters, int maxNumberOfIterations,
// DistanceStrategy distance) {

// this.D = dataPoints[0].length;
// this.K = initialClusterCenters.length;
// this.N = dataPoints.length;

// this.clusterCenters =
// Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
// this.clusterCenterMovements = new double[K];
// this.clusterSizes = new int[K];
// this.clusterSums = new double[K][D];
// this.closestOtherCenters = new int[N][B];
// this.centerOrder = new OrderedClusterCenter[K];
// this.dataPoints = dataPoints;
// this.dataPointsAssignments = new int[N];
// this.distance = distance;
// this.hasChanged = true;
// this.lowerBounds = new double[N][B];
// this.lowerBoundsAssignments = new int[N][B];
// this.furthestDistanceMoved = 0;
// this.maxNumberOfIterations = maxNumberOfIterations;
// this.numberOfIterations = 0;
// this.upperBounds = new double[N];

// final int N = dataPoints.length;
// final int D = dataPoints[0].length;

// int B = Math.max(K >> 2, 1);
// if (B < 2)
// B = 2;
// if (K <= B)
// B = K - 1;

// final int minB = Math.max(K >> 3, 1);

// Arrays.fill(dataPointsAssignments, -1);
// final CenterTuple[][] lowerBounds = new CenterTuple[N][B];
// final CenterTuple[] centerTuples = new CenterTuple[K];
// for (int c = 0; c < K; c++) {
// centerTuples[c] = new CenterTuple(0, c);
// }

// for (int n = 0; n < N; n++) {
// sortCenters(n, dataPoints[n], B, centerTuples, clusterCenters,
// dataPointsAssignments, upperBounds, lowerBounds[n],
// clusterSums, clusterSizes);
// }

// int iterations = 0;
// while (true) {
// int maxB = 0;
// outer: for (int n = 0; n < N; n++) {
// for (int b = 0; b < B; b++) {
// maxB = b > maxB ? b : maxB;
// if (upperBounds[n] < lowerBounds[n][b].distance) {
// if (b == 0)
// continue outer;
// reorderBounds(n, dataPoints[n], b, clusterCenters, dataPointsAssignments,
// upperBounds, lowerBounds[n],
// clusterSums, clusterSizes);
// continue outer;
// }
// }
// sortCenters(n, dataPoints[n], B, centerTuples, clusterCenters,
// dataPointsAssignments, upperBounds,
// lowerBounds[n], clusterSums, clusterSizes);
// }

// final double[] centerMoves = new double[K];
// double maxMoved = Double.NEGATIVE_INFINITY;

// moveCenters();
// int furthestMovingIndex = moveCenters();
// maxMoved = clusterCenterMovements[furthestMovingIndex];

// // update bounds
// for (int n = 0; n < N; n++) {
// upperBounds[n] += centerMoves[dataPointsAssignments[n]];

// lowerBounds[n][B - 1].distance -= maxMoved;
// for (int i = B - 2; i >= 0; i--) {
// lowerBounds[n][i].distance -= centerMoves[lowerBounds[n][i].index];

// if (lowerBounds[n][i].distance > lowerBounds[n][i + 1].distance) {
// lowerBounds[n][i].distance = lowerBounds[n][i + 1].distance;
// }
// }
// }

// if (iterations > 10 && maxB < B) {
// B = Math.max(maxB, minB);
// }

// iterations++;
// if (maxMoved == 0) {
// System.out.println(Arrays.toString(dataPointsAssignments));
// System.out.println(Arrays.deepToString(clusterCenters));
// return result();
// }
// }
// }

// private void insertSorted(CenterTuple c, CenterTuple[] arr) {
// for (int i = arr.length - 1; i >= 0; i--) {
// if (arr[i] == null) {
// arr[i] = c;
// break;
// } else if (arr[i].distance < c.distance) {
// // shift all others forward
// CenterTuple prev = arr[i];
// for (int j = i; j >= 1; j--) {
// if (arr[j] == null)
// break;
// CenterTuple tmp = prev;
// prev = arr[j - 1];
// arr[j - 1] = tmp;
// }
// arr[i] = c;
// break;
// }
// }
// }

// void reorderBounds(int i, double[] x, int b, double[][] centers, int[]
// assignments, double[] upperBounds,
// CenterTuple[] lowerBound, double[][] centerSum, int[] centerSize) {
// final CenterTuple[] relevantTuples = new CenterTuple[b + 1];

// relevantTuples[b] = new CenterTuple(0, 0);
// relevantTuples[b].distance = distance.compute(x, centers[assignments[i]]);
// relevantTuples[b].index = assignments[i];
// for (int j = 0; j < b; j++) {
// CenterTuple tup = new CenterTuple(0, 0);
// tup.index = lowerBound[j].index;
// tup.distance = distance.compute(x, centers[lowerBound[j].index]);

// insertSorted(tup, relevantTuples);
// }

// CenterTuple closest = relevantTuples[0];
// if (assignments[i] != closest.index) {
// removeFromCenter(x, assignments[i], centerSum, centerSize);

// assignments[i] = closest.index;

// addToCenter(x, closest.index, centerSum, centerSize);
// }
// upperBounds[i] = closest.distance;
// for (int j = 0; j < b; j++) {
// lowerBound[j] = relevantTuples[j + 1].copy();
// }
// }

// void sortCenters(int i, double[] x, int b, CenterTuple[] centerTuples,
// double[][] centers, int[] assignments,
// double[] upperB, CenterTuple[] lowerB, double[][] centerSum, int[]
// centerSize) {
// final CenterTuple[] sortedTup = new CenterTuple[centers.length];
// for (int j = 0; j < centers.length; j++) {
// double dist = distance.compute(x, centers[j]);
// CenterTuple tup = centerTuples[j];
// tup.distance = dist;
// tup.index = j;

// insertSorted(tup, sortedTup);
// }

// CenterTuple closest = sortedTup[0];
// if (assignments[i] != closest.index) {
// if (assignments[i] != -1)
// removeFromCenter(x, assignments[i], centerSum, centerSize);

// assignments[i] = closest.index;

// addToCenter(x, closest.index, centerSum, centerSize);
// }
// upperB[i] = closest.distance;

// for (int j = 0; j < b; j++) {
// lowerB[j] = sortedTup[j + 1].copy();
// }
// }

// private void removeFromCenter(double[] x, int assignment, double[][]
// centerSum, int[] centerSize) {
// for (int d = 0; d < centerSum[assignment].length; d++) {
// centerSum[assignment][d] -= x[d];
// }
// centerSize[assignment]--;
// }

// private void addToCenter(double[] x, int assignment, double[][] centerSum,
// int[] centerSize) {
// for (int d = 0; d < centerSum[assignment].length; d++) {
// centerSum[assignment][d] += x[d];
// }
// centerSize[assignment]++;
// }

// @Override
// protected void initialize() {

// }

// @Override
// protected void loop() {

// }

// }
