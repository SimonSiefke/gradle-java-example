import java.util.Arrays;

import kmeans.Cluster;
import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;
import dataloader.DataLoader;

public final class Main {
  private Main() {
    // no public constructor
  }

  public static void main(String[] args) {
    var its = 7;
    // var inputData = new double[][] { { 0, 2 }, { 0, 8 } };
    // var initialClusterCenters = new double[][] { { 0, 0 }, { 0, 1 } };
    var inputData = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { inputData[0], inputData[1] };
    Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData, initialClusterCenters, its);
    Cluster[] clusters2 = new ElkanKMeansStrategy().cluster(inputData, initialClusterCenters, its);
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    Arrays.sort(clusters2, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    System.out.println("Lloyd");
    System.out.println(Arrays.toString(clusters[0].center));
    System.out.println(Arrays.toString(clusters[1].center));
    System.out.println('\n');
    System.out.println("elkan");
    System.out.println(Arrays.toString(clusters2[0].center));
    System.out.println(Arrays.toString(clusters2[1].center));

    // var inputData = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    // var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    // Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData,
    // initialClusterCenters, 1);
    // Arrays.sort(clusters, (clusterA, clusterB) ->
    // Double.compare(clusterA.center[0], clusterB.center[0]));

    // int nums = 1;

    // var inputData = new double[][] { { 3, 4 }, { 0, 6 }, { 0, 0 } };
    // var initialClusterCenters = new double[][] { { 3, 4 }, { 0, 6 } };
    // Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData,
    // initialClusterCenters, nums);
    // Cluster[] clusters2 = new ElkanKMeansStrategy().cluster(inputData,
    // initialClusterCenters, nums);
    // // Arrays.sort(clusters, (clusterA, clusterB) ->
    // // Double.compare(clusterA.center[0], clusterB.center[0]));
    // Arrays.sort(clusters2, (clusterA, clusterB) ->
    // Double.compare(clusterA.center[0], clusterB.center[0]));

    // // System.out.println("lloyd");
    // // System.out.println(Arrays.toString(clusters[0].center));
    // // System.out.println(Arrays.toString(clusters[1].center));
    // // // System.out.println(Arrays.toString(clusters[2].center));
    // System.out.println('\n');
    // System.out.println("elkan");
    // System.out.println(Arrays.toString(clusters2[0].center));
    // System.out.println(Arrays.toString(clusters2[1].center));
    // var inputData = DataLoader.TEXT("../benchmark/data/A1.txt");
    // var initialClusterCenters = new double[][] { inputData[0], inputData[1] };
    // Cluster[] clusters = new ElkanKMeansStrategy().cluster(inputData,
    // initialClusterCenters, 100);
    // // Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData,
    // // initialClusterCenters, 100);
    // Arrays.sort(clusters, (clusterA, clusterB) ->
    // Double.compare(clusterA.center[0], clusterB.center[0]));

    // assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center,
    // 0.001);
    // assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center,
    // 0.001);
    // var inputData = new double[][] { { 0, 0 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
    // var initialClusterCenters = new double[][] { { 0, 0 }, { 4, 4 } };
    // Cluster[] clusters = new ElkanKMeansStrategy().cluster(inputData,
    // initialClusterCenters, 1);
    // // Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData,
    // // initialClusterCenters, 5);

    // // var inputData = new double[][] { { 0, 0 } };
    // // var initialClusterCenters = new double[][] { { 1, 1 } };
    // // Cluster[] clusters = new ElkanKMeansStrategy().cluster(inputData,
    // // initialClusterCenters, 1);
    // System.out.println('\n');

    // var inputData = new double[][] { { 0, 0 }, { 2, 2 } };
    // var inputData = DataLoader.TEXT("../benchmark/data/A1.txt");
    // var initialClusterCenters = new double[][] { inputData[0], inputData[1] };
    // Cluster[] clusters = new LloydKMeansStrategy().cluster(inputData,
    // initialClusterCenters, 100);
    // Arrays.sort(clusters, (clusterA, clusterB) ->
    // Double.compare(clusterA.center[0], clusterB.center[0]));

    // // System.out.println(clusters[0].center[1]);
    // System.out.println(Arrays.toString(new double[] { 1, 1 }));
    // System.out.println(Arrays.toString(clusters[0].center));
    // System.out.println(Arrays.toString(clusters[1].center));

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
