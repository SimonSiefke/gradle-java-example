import java.util.Arrays;

import dataloader.DataLoader;
import distance.EuclideanDistanceStrategy;
import kmeans.Cluster;
import kmeans.hamerly.HamerlyKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {

    var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    Cluster[] clusters = new HamerlyKMeansStrategy().cluster(dataPoints, initialClusterCenters, Integer.MAX_VALUE,
        new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    // assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center,
    // 0.001);
    // assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center,
    // 0.001);

    // var dataPoints = new double[][] { { 0, 0 } };
    // var initialClusterCenters = new double[][] { { 1, 1 } };
    // Cluster[] clusters = new HamerlyKMeansStrategy().cluster(dataPoints,
    // initialClusterCenters, 100,
    // new EuclideanSquaredDistanceStrategy());

    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');

  }
}
