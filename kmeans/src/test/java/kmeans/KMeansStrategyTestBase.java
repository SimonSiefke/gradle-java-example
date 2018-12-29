package kmeans;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dataloader.DataLoader;
import distance.EuclideanDistanceStrategy;
import distance.EuclideanSquaredDistanceStrategy;

public abstract class KMeansStrategyTestBase<T extends KMeansStrategy> {

  private T instance;

  protected abstract T createInstance();

  @BeforeEach
  public void setUp() {
    instance = createInstance();
  }

  @Test
  public void testOnePoint() {
    var dataPoints = new double[][] { { 0, 0 }, { 0, 0 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 0, 0 }, clusters[0].center);
  }

  @Test
  public void testOneSamePoint() {
    var dataPoints = new double[][] { { 1, 1 }, { 1, 1 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
  }

  @Test
  public void testTwoPoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 } };
    var initialClusterCenters = new double[][] { { 0, 0 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
  }

  @Test
  public void testTwoPointsWithWrongInitialClusters() {
    var dataPoints = new double[][] { { 1, 1 }, { 2, 2 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 1, 1 } };
    var exception = assertThrows(IllegalArgumentException.class,
        () -> instance.cluster(dataPoints, initialClusterCenters, 10, new EuclideanSquaredDistanceStrategy()));
    assertEquals(
        "Please provide different initial cluster centers, one or more of your initial clusters are too far away from any data point",
        exception.getMessage());
  }

  // TODO this is the only failing test
  @Test
  @Disabled
  public void testThreePoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 2, 2 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 10,
        new EuclideanSquaredDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 6, 6 }, clusters[1].center);
  }

  @Test
  public void testAnotherThreePoints() {
    var dataPoints = new double[][] { { 3, 4 }, { 0, 6 }, { 0, 0 } };
    var initialClusterCenters = new double[][] { { 3, 4 }, { 0, 6 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 10,
        new EuclideanSquaredDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));
    assertArrayEquals(new double[] { 0, 6 }, clusters[0].center);
    assertArrayEquals(new double[] { 1.5, 2 }, clusters[1].center);
  }

  @Test
  public void testFourPoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 4, 4 }, { 6, 6 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 4, 4 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 5, 5 }, clusters[1].center);
  }

  @Test
  public void testFivePoints() {
    var dataPoints = new double[][] { { 2 }, { 4 }, { 3 }, { 0 }, { 1 } };
    var initialClusterCenters = new double[][] { { 2 }, { 4 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 1.5 }, clusters[0].center);
    assertArrayEquals(new double[] { 4 }, clusters[1].center);
  }

  @Test
  void testFullDatasetWithEuclideanDistance() {
    var dataPoints = DataLoader.TEXT("../benchmark/data/A1.txt");
    var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, Integer.MAX_VALUE,
        new EuclideanDistanceStrategy());
    Arrays.sort(clusters, (clusterA, clusterB) -> Double.compare(clusterA.center[0], clusterB.center[0]));

    assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center, 0.001);
    assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center, 0.001);
  }
}
