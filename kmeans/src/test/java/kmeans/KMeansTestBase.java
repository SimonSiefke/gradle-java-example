package kmeans;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataloader.DataLoader;

public abstract class KMeansTestBase<T extends KMeansStrategy> {

  private T instance;

  protected abstract T createInstance();

  @BeforeEach
  public void setUp() {
    instance = createInstance();
  }

  // TODO remove useless and redundant tests
  // TODO add test where initial cluster centers have no close points

  @Test
  public void testOnePoint() {
    var inputData = new double[][] { { 0, 0 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    Cluster[] clusters = instance.cluster(inputData, initialClusterCenters, 1);
    assertArrayEquals(new double[] { 0, 0 }, clusters[0].center);
  }

  @Test
  public void testTwoPoints() {
    var inputData = new double[][] { { 0, 0 }, { 2, 2 } };
    var initialClusterCenters = new double[][] { { 0, 0 } };
    Cluster[] clusters = instance.cluster(inputData, initialClusterCenters, 1);
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
  }

  @Test
  public void testThreePoints() {
    var inputData = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    Cluster[] clusters = instance.cluster(inputData, initialClusterCenters, 10);
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 6, 6 }, clusters[1].center);
  }

  @Test
  public void testAnotherThreePoints() {
    var inputData = new double[][] { { 3, 4 }, { 0, 6 }, { 0, 0 } };
    var initialClusterCenters = new double[][] { { 3, 4 }, { 0, 6 } };
    Cluster[] clusters = instance.cluster(inputData, initialClusterCenters, 10);
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 0, 6 }, clusters[0].center);
    assertArrayEquals(new double[] { 1.5, 2 }, clusters[1].center);
  }

  @Test
  public void testFourPoints() {
    var inputData = new double[][] { { 0, 0 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 4, 4 } };
    Cluster[] clusters = instance.cluster(inputData, initialClusterCenters, 1);
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 5, 5 }, clusters[1].center);
  }

  @Test
  void testFullDataset() {
    var inputData = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { inputData[0], inputData[1] };
    Cluster[] clusters = instance.cluster(inputData, initialClusterCenters, 100);
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center, 0.001);
    assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center, 0.001);
  }
}
