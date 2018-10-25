import java.util.Arrays;

import dataLoader.DataLoader;
import distance.DISTANCE_STRATEGY;
import distance.EuclideanDistanceStrategy;
import distance.EuclideanSquaredDistanceStrategy;
import kmeans.KMEANS_STRATEGY;
import kmeans.KMeansBuilder;
import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {
    var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    var clusters = new ElkanKMeansStrategy().cluster(dataPoints, initialClusterCenters, 60,
        new EuclideanSquaredDistanceStrategy());
    var clusters2 = new KMeansBuilder().withDataPoints(dataPoints).withStrategy(KMEANS_STRATEGY.ELKAN)
        .withInitialClusterCenters(initialClusterCenters).withMaxNumberOfIterations(60)
        .withDistance(DISTANCE_STRATEGY.EUCLIDEAN_SQUARED).cluster();

    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');
    Arrays.sort(clusters2, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    System.out.println(Arrays.toString(clusters2[0].center));
    System.out.println(Arrays.toString(clusters2[1].center));

  }
}
