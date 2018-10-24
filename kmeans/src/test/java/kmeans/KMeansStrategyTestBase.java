package kmeans;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataLoader.DataLoader;
import distance.EuclideanDistanceStrategy;

public abstract class KMeansStrategyTestBase<T extends KMeansStrategy> {

  private T instance;

  protected abstract T createInstance();

  @BeforeEach
  public void setUp() {
    instance = createInstance();
  }

  // TODO remove useless and redundant tests
  // TODO add test where initial cluster centers have no close points
  // TODO
  // definitly test those
  // 2
  // 4
  // 3
  // 0
  // 1

  @Test
  public void testOnePoint() {
    var dataPoints = new double[][] { { 0, 0 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanDistanceStrategy());
    assertArrayEquals(new double[] { 0, 0 }, clusters[0].center);
  }

  @Test
  public void testTwoPoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 } };
    var initialClusterCenters = new double[][] { { 0, 0 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanDistanceStrategy());
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
  }

  @Test
  public void testThreePoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 10, new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 6, 6 }, clusters[1].center);
  }

  @Test
  public void testAnotherThreePoints() {
    var dataPoints = new double[][] { { 3, 4 }, { 0, 6 }, { 0, 0 } };
    var initialClusterCenters = new double[][] { { 3, 4 }, { 0, 6 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 10, new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 0, 6 }, clusters[0].center);
    assertArrayEquals(new double[] { 1.5, 2 }, clusters[1].center);
  }

  @Test
  public void testFourPoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 4, 4 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanDistanceStrategy());
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 5, 5 }, clusters[1].center);
  }

  @Test
  void testFullDataset() {
    var dataPoints = DataLoader.TEXT("../kmeans/src/test/data/test_kmeans.txt");
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 100, new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center, 0.001);
    assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center, 0.001);
  }
}
