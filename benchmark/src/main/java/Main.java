// import java.util.Arrays;

// import dataloader.DataLoader;
// import distance.EuclideanDistanceStrategy;
// import kmeans.Cluster;
// import kmeans.hamerly.HamerlyKMeansStrategy;
// import kmeans.lloyd.LloydKMeansStrategy;

// public final class Main {
// private Main() {
// // no public constructor
// }

// public static void main(String[] args) {
// var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
// var initialClusterCenters = new double[][] { { 0, 0 }, { 4, 4 } };
// Cluster[] clusters = new LloydKMeansStrategy().cluster(dataPoints,
// initialClusterCenters, 1,
// new EuclideanDistanceStrategy());
// // assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
// // assertArrayEquals(new double[] { 5, 5 }, clusters[1].center);

// System.out.println(Arrays.toString(clusters[0].center));
// System.out.println(Arrays.toString(clusters[1].center));
// System.out.println('\n');

// }
// }
