package kmeans.lloyd;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import kmeans.Cluster;
import kmeans.KMeans;
import util.dataloader.DataLoader;

public class LloydKMeansStrategyTest {

  @Test
  public void testOnePoint() {
    var inputData = new double[][] { { 0, 0 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    Cluster[] clusters = KMeans.LLOYD(inputData, initialClusterCenters, 1);
    assertArrayEquals(new double[] { 0, 0 }, clusters[0].center);
  }

  @Test
  public void testTwoPoints() {
    var inputData = new double[][] { { 0, 0 }, { 2, 2 } };
    var initialClusterCenters = new double[][] { { 0, 0 } };
    Cluster[] clusters = KMeans.LLOYD(inputData, initialClusterCenters, 1);
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
  }

  @Test
  void testFullDataset() {
    var inputData = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { inputData[0], inputData[1] };
    Cluster[] clusters = KMeans.LLOYD(inputData, initialClusterCenters, 100);
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center, 0.001);
    assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center, 0.001);
  }
}
