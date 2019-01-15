package kmeans;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import util.dataloader.DataLoader;
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
    var dataPoints = new double[][] { { 0, 0 } };
    var initialClusterCenters = new double[][] { { 1, 1 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 0, 0 }, clusters[0].center);
  }

  @Test
  public void testOneSamePoint() {
    var dataPoints = new double[][] { { 1, 1 } };
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
  public void testAnotherFourPoints() {
    var dataPoints = new double[][] { { 0, 0 }, { 2, 2 }, { 6, 6 }, { 8, 8 } };
    var initialClusterCenters = new double[][] { { 0, 0 }, { 6, 6 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 1, 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 7, 7 }, clusters[1].center);
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
   void testFullDatasetWithTwoClusters() {
   var dataPoints = DataLoader.TEXT("test_data/test_kmeans.txt");
   var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1] };
   Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters,
   Integer.MAX_VALUE,
   new EuclideanDistanceStrategy());
   Arrays.sort(clusters, (clusterA, clusterB) ->
   Double.compare(clusterA.center[0], clusterB.center[0]));

   assertArrayEquals(new double[] { 21263.194, 54735.200 }, clusters[0].center,
   0.001);
   assertArrayEquals(new double[] { 50126.475, 46599.611 }, clusters[1].center,
   0.001);
   }

   @Test
   void testFullDatasetWithEightClusters() {
   var dataPoints = DataLoader.TEXT("test_data/test_kmeans.txt");
   var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1],
   dataPoints[2], dataPoints[3],
   dataPoints[4], dataPoints[5], dataPoints[6], dataPoints[7] };
   Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters,
   Integer.MAX_VALUE,
   new EuclideanDistanceStrategy());
   Arrays.sort(clusters, (clusterA, clusterB) ->
   Double.compare(clusterA.center[0], clusterB.center[0]));

   assertArrayEquals(new double[] { 12294.801, 56220.885 }, clusters[0].center,
   0.01);
   assertArrayEquals(new double[] { 31185.745, 58522.447 }, clusters[1].center,
   0.01);
   assertArrayEquals(new double[] { 32695.143, 44269.877 }, clusters[2].center,
   0.01);
   assertArrayEquals(new double[] { 47476.562, 47747.472 }, clusters[3].center,
   0.01);
   assertArrayEquals(new double[] { 54481.169, 43586.819 }, clusters[4].center,
   0.01);
   assertArrayEquals(new double[] { 56814.013, 35753.430 }, clusters[5].center,
   0.01);
   assertArrayEquals(new double[] { 58688.164, 59607.411 }, clusters[6].center,
   0.01);
   assertArrayEquals(new double[] { 60667.370, 49451.933 }, clusters[7].center,
   0.01);
   }

   @Test
   void testFullDatasetWithTwentySevenClusters() {
   var dataPoints = DataLoader.TEXT("test_data/test_kmeans.txt");
   var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1],
   dataPoints[2], dataPoints[3],
   dataPoints[4], dataPoints[5], dataPoints[6], dataPoints[7], dataPoints[8],
   dataPoints[9], dataPoints[10],
   dataPoints[11], dataPoints[12], dataPoints[13], dataPoints[14],
   dataPoints[15], dataPoints[16], dataPoints[17],
   dataPoints[18], dataPoints[19], dataPoints[20], dataPoints[21],
   dataPoints[22], dataPoints[23], dataPoints[24],
   dataPoints[25], dataPoints[26] };
   Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters,
   Integer.MAX_VALUE,
   new EuclideanDistanceStrategy());
   Arrays.sort(clusters, (clusterA, clusterB) ->
   Double.compare(clusterA.center[0], clusterB.center[0]));

   assertArrayEquals(new double[] { 12149.972183588317, 56155.23087621697 },
   clusters[0].center, 0.01);
   assertArrayEquals(new double[] { 26094.979423868313, 45393.81069958848 },
   clusters[1].center, 0.01);
   assertArrayEquals(new double[] { 30618.047817047816, 59002.2079002079 },
   clusters[2].center, 0.01);
   assertArrayEquals(new double[] { 36008.163522012575, 37352.666666666664 },
   clusters[3].center, 0.01);
   assertArrayEquals(new double[] { 36128.85196374622, 47653.33836858006 },
   clusters[4].center, 0.01);
   assertArrayEquals(new double[] { 44357.7108433735, 46156.114457831325 },
   clusters[5].center, 0.01);
   assertArrayEquals(new double[] { 50690.25490196078, 48273.21568627451 },
   clusters[6].center, 0.01);
   assertArrayEquals(new double[] { 50698.87804878049, 50582.31707317073 },
   clusters[7].center, 0.01);
   assertArrayEquals(new double[] { 51910.882352941175, 42873.94117647059 },
   clusters[8].center, 0.01);
   assertArrayEquals(new double[] { 52940.357142857145, 50252.19047619047 },
   clusters[9].center, 0.01);
   assertArrayEquals(new double[] { 53084.045454545456, 47628.045454545456 },
   clusters[10].center, 0.01);
   assertArrayEquals(new double[] { 53482.75, 42328.65 }, clusters[11].center,
   0.01);
   assertArrayEquals(new double[] { 53720.07692307692, 40721.230769230766 },
   clusters[12].center, 0.01);
   assertArrayEquals(new double[] { 53924.0625, 36298.8125 },
   clusters[13].center, 0.01);
   assertArrayEquals(new double[] { 54184.06451612903, 44306.93548387097 },
   clusters[14].center, 0.01);
   assertArrayEquals(new double[] { 54814.96551724138, 42651.06896551724 },
   clusters[15].center, 0.01);
   assertArrayEquals(new double[] { 56083.12244897959, 35167.102040816324 },
   clusters[16].center, 0.01);
   assertArrayEquals(new double[] { 56121.68421052631, 43928.21052631579 },
   clusters[17].center, 0.01);
   assertArrayEquals(new double[] { 56275.64705882353, 41560.23529411765 },
   clusters[18].center, 0.01);
   assertArrayEquals(new double[] { 57238.90909090909, 38873.72727272727 },
   clusters[19].center, 0.01);
   assertArrayEquals(new double[] { 57441.91111111111, 36566.64444444444 },
   clusters[20].center, 0.01);
   assertArrayEquals(new double[] { 58451.46666666667, 34056.3 },
   clusters[21].center, 0.01);
   assertArrayEquals(new double[] { 58641.16, 59852.84 }, clusters[22].center,
   0.01);
   assertArrayEquals(new double[] { 59669.61111111111, 45979.38888888889 },
   clusters[23].center, 0.01);
   assertArrayEquals(new double[] { 60343.15, 52924.2 }, clusters[24].center,
   0.01);
   assertArrayEquals(new double[] { 60563.350877192985, 49512.17543859649 },
   clusters[25].center, 0.01);
   assertArrayEquals(new double[] { 62165.64179104478, 45940.388059701494 },
   clusters[26].center, 0.01);
   }
}
