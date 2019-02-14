package kmeans.lloyd;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import distance.EuclideanSquaredDistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategyTestBase;

public class FastLloydKMeansStrategyTest extends KMeansStrategyTestBase<FastLloydKMeansStrategy> {
  @Override
  protected FastLloydKMeansStrategy createInstance() {
    return new FastLloydKMeansStrategy();
  }

  @Test
  @Override
  public void testFivePoints() {
    var dataPoints = new double[][] { { 2 }, { 4 }, { 3 }, { 0 }, { 1 } };
    var initialClusterCenters = new double[][] { { 2 }, { 4 } };
    Cluster[] clusters = instance.cluster(dataPoints, initialClusterCenters, 1, new EuclideanSquaredDistanceStrategy());
    assertArrayEquals(new double[] { 1 }, clusters[0].center);
    assertArrayEquals(new double[] { 3.5 }, clusters[1].center);
  }

}
