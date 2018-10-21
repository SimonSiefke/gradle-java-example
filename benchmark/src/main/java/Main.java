import java.util.Arrays;

import kmeans.Cluster;
import kmeans.lloyd.LloydKMeansStrategy;
import util.dataloader.DataLoader;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {
    // var inputData = new double[][] { { 0, 0 }, { 2, 2 } };
    var inputData = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { inputData[0], inputData[1] };
    Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData, initialClusterCenters, 100);
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    // System.out.println(clusters[0].center[1]);
    System.out.println(Arrays.toString(new double[] { 1, 1 }));
    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));

    // should be
    // 21263.194,54735.200
    // 50126.475,46599.611
    // for(var p : new double[]{0,0}){
    // System.out.println(p);
    // System.out.println();
    // }

    // System.out.println(Arrays.deepToString(clusters[0].center));
    // assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    // System.out.println(clusters[0].center);
    // assertEquals(1, 1);
  }
}
