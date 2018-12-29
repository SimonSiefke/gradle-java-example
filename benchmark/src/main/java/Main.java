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
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1], dataPoints[2], dataPoints[3],
        dataPoints[4], dataPoints[5], dataPoints[6], dataPoints[7] };
    //
    //
    System.out.println(Arrays.deepToString(initialClusterCenters));
    Cluster[] clusters = new HamerlyKMeansStrategy().cluster(dataPoints, initialClusterCenters, 100,
        new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    //
    //
    //

    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    System.out.println(Arrays.toString(clusters[2].center));
    System.out.println(Arrays.toString(clusters[3].center));
    System.out.println(Arrays.toString(clusters[4].center));
    System.out.println(Arrays.toString(clusters[5].center));
    System.out.println(Arrays.toString(clusters[6].center));
    System.out.println(Arrays.toString(clusters[7].center));
    System.out.println('\n');

  }
}
