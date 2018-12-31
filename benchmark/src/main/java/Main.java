import java.util.Arrays;

import distance.EuclideanDistanceStrategy;
import kmeans.Cluster;
// import kmeans.drake.DrakeKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {

    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 4, 4 } };
    //
    //
    Cluster[] clusters = new LloydKMeansStrategy().cluster(dataPoints, initialClusterCenters, 100,
        new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    //
    //
    //

    for (var c : clusters) {
      System.out.println(Arrays.toString(c.center));

    }
    System.out.println('\n');
  }
}
