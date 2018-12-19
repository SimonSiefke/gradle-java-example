import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import dataloader.DataLoader;
import distance.EuclideanDistanceStrategy;
import kmeans.Cluster;
import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.hamerly.HamerlyKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {

    var dataPoints = new double[][] { { 1, 1 }, { 2, 2 } };
    var initialClusterCenters = new double[][] { { 2, 2 }, { 1, 1 } };

    Cluster[] clusters = new LloydKMeansStrategy().cluster(dataPoints, initialClusterCenters, 10,
        new EuclideanDistanceStrategy());
    // // assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    // // assertArrayEquals(new double[] { 5, 5 }, clusters[1].center);

    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    // System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');

  }
}
