import java.util.Arrays;

import dataLoader.DataLoader;
import distance.EuclideanDistanceStrategy;
import distance.EuclideanSquaredDistanceStrategy;
import kmeans.KMEANS_STRATEGY;
import kmeans.KMeansBuilder;
import kmeans.lloyd.LloydKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {
    var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    // var dataPoints = new double[][] { { 0, 0 }, { 2, 2 } };
    // var initialClusterCenters = new double[][] { { 0, 0 } };
    // var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    // var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    // var dataPoints = new double[][] { { 1, 1 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
    // var initialClusterCenters = new double[][] { { 1, 1 }, { 4, 4 } };
    var clusters2 = new KMeansBuilder().withDataPoints(dataPoints).withStrategy(KMEANS_STRATEGY.LLOYD)
        .withInitialClusterCenters(initialClusterCenters).withMaxNumberOfIterations(20).cluster();

    var clusters = new LloydKMeansStrategy().cluster(dataPoints, initialClusterCenters, 20,
        new EuclideanSquaredDistanceStrategy());

    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');
    Arrays.sort(clusters2, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println(Arrays.toString(clusters2[0].center));
    System.out.println(Arrays.toString(clusters2[1].center));
    // var dataPoints = DataLoader.TEXT("../benchmark/data/census_data.txt");
    // var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    // var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    // var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    // // var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    // var kmeans = new
    // KMeansBuilder().withDataPoints(dataPoints).withInitialClusterCenters(initialClusterCenters);

    // var clusters = kmeans.withStrategy(KMEANS_STRATEGY.LLOYD).cluster();
    // Arrays.sort(clusters, (clusterA, clusterB) ->
    // Double.compare(clusterA.center[0], clusterB.center[0]));
    // System.out.println("Lloyd");
    // System.out.println(Arrays.toString(clusters[0].center));
    // System.out.println(Arrays.toString(clusters[1].center));
    // System.out.println('\n');
    // var clusters2 = kmeans.withStrategy(KMEANS_STRATEGY.ELKAN).cluster();
    // Arrays.sort(clusters2, (clusterA, clusterB) ->
    // Double.compare(clusterA.center[0], clusterB.center[0]));
    // System.out.println("elkan");
    // System.out.println(Arrays.toString(clusters2[0].center));
    // System.out.println(Arrays.toString(clusters2[1].center));

  }
}
