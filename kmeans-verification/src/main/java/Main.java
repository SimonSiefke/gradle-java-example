import java.util.Arrays;

import distance.EuclideanDistanceStrategy;
import kmeans.Cluster;
import kmeans.hamerly.HamerlyKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {
    var dataPoints = new double[][] { { 4, 0 }, { 5, 0 }, { 0, -3 }, { -1, 0 }, { 0, 0 }, { 0, 1 }, { 1, 0 } };
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1], dataPoints[2] };

    // //
    // //
    Cluster[] clusters = new HamerlyKMeansStrategy().cluster(dataPoints, initialClusterCenters, Integer.MAX_VALUE,
        new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    //
    //
    //

    // for (var c : clusters) {
    // System.out.println(Arrays.toString(c.center));
    // System.out.println(c.closestPoints.size());

    // }
    // System.out.println('\n');
  }
}
