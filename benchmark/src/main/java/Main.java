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

    var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    //
    //
    // Cluster[] clusters = new MacQueenKMeansStrategy().cluster(dataPoints, initialClusterCenters, 1,
        // new EuclideanDistanceStrategy());

    //
    //
    //

    // System.out.println(Arrays.toString(clusters[0].center));
    // System.out.println(Arrays.toString(clusters[1].center));
    // System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');

  }
}
