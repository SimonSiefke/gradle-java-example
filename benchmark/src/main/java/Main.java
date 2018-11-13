// import java.util.*;
// import distance.*;
// import kmeans.*;
// import kmeans.Cluster;
// import dataloader.DataLoader;
// import kmeans.elkan.ElkanKMeansStrategy;
// import kmeans.lloyd.LloydKMeansStrategy;
// import kmeans.lloyd.LloydSingle;

// public final class Main {
// private Main() {
// // no public constructor
// }

// public static void main(String[] args) {
// double[][] dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
// // var dataPoints = new double[][] { { 2 }, { 4 }, { 3 }, { 0 }, { 1 } };

// var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
// var initialClusterCenters2 = new double[][] { dataPoints[0], dataPoints[1] };
// int n = Integer.MAX_VALUE;
// Cluster[] clusters = new LloydSingle().cluster(dataPoints,
// initialClusterCenters, n,
// new EuclideanSquaredDistanceStrategy());
// Cluster[] clusters2 = new LloydKMeansStrategy().cluster(dataPoints,
// initialClusterCenters2, n,
// new EuclideanSquaredDistanceStrategy());
// // var clusters2 = new
// //
// KMeansBuilder().withDataPoints(dataPoints).withStrategy(KMEANS_STRATEGY.ELKAN)
// //
// .withInitialClusterCenters(initialClusterCenters).withMaxNumberOfIterations(60)
// // .withDistance(DISTANCE_STRATEGY.EUCLIDEAN_SQUARED).cluster();

// Arrays.sort(clusters2, (clusterA, clusterB) ->
// Double.compare(clusterA.center[0], clusterB.center[0]));
// System.out.println(Arrays.toString(clusters2[0].center));
// System.out.println(Arrays.toString(clusters2[1].center));
// System.out.println('\n');

// Arrays.sort(clusters, (clusterA, clusterB) ->
// Double.compare(clusterA.center[0], clusterB.center[0]));
// System.out.println(Arrays.toString(clusters[0].center));
// System.out.println(Arrays.toString(clusters[1].center));

// }
// }
