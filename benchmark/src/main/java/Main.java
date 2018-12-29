import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import dataloader.DataLoader;
import distance.EuclideanDistanceStrategy;
import kmeans.Cluster;
import kmeans.drake.DrakeKMeansStrategy;
import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.hamerly.HamerlyKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {

    var dataPoints = new double[][] { { 0, 0 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    //
    //
    Cluster[] clusters = new ElkanKMeansStrategy().cluster(dataPoints, initialClusterCenters, 100,
        new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    //
    //
    //

    System.out.println(Arrays.toString(clusters[0].center));
    // System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');

  }
}
