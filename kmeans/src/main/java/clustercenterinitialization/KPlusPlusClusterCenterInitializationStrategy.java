// package clustercenterinitialization;

// import javax.annotation.Nonnull;

// import java.util.Random;

// import distance.DistanceStrategy;

// /**
// * K++ Cluster Center Initialization Strategy.
// */
// public class KPlusPlusClusterCenterInitializationStrategy implements
// ClusterCenterInitializationStrategy {
// @Override
// public double[][] initialize(@Nonnull double[][] dataPoints, int K, @Nonnull
// DistanceStrategy distance) {
// int N = dataPoints.length;
// var centroids = new double[K][N];
// var distToClosestCentroid = new double[K];
// var weightedDistribution = new double[K]; // cumulative sum of squared
// distances

// Random randomGenerator = new Random();
// int choose = 0;

// for (int k = 0; k < K; k++) {

// // first centroid: choose any data point
// if (k == 0)
// choose = randomGenerator.nextInt(K);

// // after first centroid, use a weighted distribution
// else {

// // check if the most recently added centroid is closer to any of the points
// than
// // previously added ones
// for (int l = 0; l < K; l++) {
// // gives chosen points 0 probability of being chosen again -> sampling
// without
// // replacement
// double tempDistance = distance.compute(dataPoints[l], centroids[k - 1]); //
// need L2 norm here, not L1

// // base case: if we have only chosen one centroid so far, nothing to compare
// to
// if (k == 1)
// distToClosestCentroid[l] = tempDistance;

// else { // c != 1
// if (tempDistance < distToClosestCentroid[l])
// distToClosestCentroid[l] = tempDistance;
// }

// // no need to square because the distance is the square of the euclidean dist
// if (l == 0)
// weightedDistribution[0] = distToClosestCentroid[0];
// else
// weightedDistribution[l] = weightedDistribution[l - 1] +
// distToClosestCentroid[l];

// }

// // choose the next centroid
// double rand = randomGenerator.nextDouble();
// for (int j = K - 1; j > 0; j--) {
// // TODO: review and try to optimize
// // starts at the largest bin. EDIT: not actually the largest
// if (rand > weightedDistribution[j - 1] / weightedDistribution[K - 1]) {
// choose = j; // one bigger than the one above
// break;
// } else // Because of invalid dimension errors, we can't make the forloop go
// to j2 > -1
// // when we have (j2-1) in the loop.
// choose = 0;
// }
// }

// // store the chosen centroid
// for (int i = 0; i < N; i++) {
// centroids[k][i] = dataPoints[choose][i];
// }
// }

// return centroids;
// }
// }
