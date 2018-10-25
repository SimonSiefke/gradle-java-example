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
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    var clusters = new KMeansBuilder().withDataPoints(dataPoints).withStrategy(KMEANS_STRATEGY.ELKAN)
        .withInitialClusterCenters(initialClusterCenters).withMaxNumberOfIterations(10).cluster();

    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));

  }
}
