import java.util.Arrays;

import dataLoader.DataLoader;
import kmeans.KMEANS_STRATEGY;
import kmeans.KMeansBuilder;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {
    // var dataPoints = DataLoader.TEXT("../benchmark/data/census_data.txt");
    // var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    // var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    var kmeans = new KMeansBuilder().withDataPoints(dataPoints).withNumberOfClusters(2);

    var clusters = kmeans.withStrategy(KMEANS_STRATEGY.LLOYD).cluster();
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println("Lloyd");
    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');
    var clusters2 = kmeans.withStrategy(KMEANS_STRATEGY.ELKAN).cluster();
    Arrays.sort(clusters2, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println("elkan");
    System.out.println(Arrays.toString(clusters2[0].center));
    System.out.println(Arrays.toString(clusters2[1].center));

  }
}
