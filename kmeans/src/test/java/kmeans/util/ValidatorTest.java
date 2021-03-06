package kmeans.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import kmeans.Validator;
import org.junit.jupiter.api.Test;

import clustercenterinitialization.ClusterCenterInitializationStrategy;
import kmeans.KMeansStrategy;

public class ValidatorTest {

  @Test
  public void testNullDataset() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateClusterDataPointsAndClusterCenters(null, new double[][] { { 1 } }));
  }

  @Test
  public void testEmptyDataset() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateClusterDataPointsAndClusterCenters(new double[][] {}, new double[][] {}));
  }

  @Test
  public void testWrongDimensionsDataset1() {
    assertThrows(IllegalArgumentException.class, () -> Validator
        .validateClusterDataPointsAndClusterCenters(new double[][] { { 1 }, {} }, new double[][] { { 2 } }));
  }

  @Test
  public void testWrongDimensionsDataset2() {
    assertThrows(IllegalArgumentException.class, () -> Validator
        .validateClusterDataPointsAndClusterCenters(new double[][] { { 1 }, { 2 } }, new double[][] { { 3, 4 } }));
  }

  @Test
  public void testWrongDimensionsAndNullDataset() {
    assertThrows(IllegalArgumentException.class, () -> Validator
        .validateClusterDataPointsAndClusterCenters(new double[][] { null, { 2, 3 } }, new double[][] { { 4, 5 } }));
  }

  @Test
  public void testCorrectDimensionsDataset() {
    Validator.validateClusterDataPointsAndClusterCenters(new double[][] { { 0 } }, new double[][] { { 1 } });
  }

  @Test
  public void testEmptyClusterCenters() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateClusterDataPointsAndClusterCenters(new double[][] {}, new double[][] {}));
  }

  @Test
  public void testNullClusterCenters() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateClusterDataPointsAndClusterCenters(null, new double[][] { { 1 } }));
  }

  @Test
  public void testWrongClusterCenters1() {
    assertThrows(IllegalArgumentException.class, () -> Validator
        .validateClusterDataPointsAndClusterCenters(new double[][] { { 1 } }, new double[][] { { 2 }, { 3, 4 } }));
  }

  @Test
  public void testWrongClusterCenters2() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateClusterDataPointsAndClusterCenters(new double[][] { null, { 1 } },
            new double[][] { { 2 }, { 3, 4 } }));
  }

  @Test
  public void testWrongNumberOfIterations() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateNumberOfIterations(-1));
  }

  @Test
  public void testNullKMeansStrategy() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateKMeansStrategy(null));
  }

  @Test
  public void testValidKMeansStrategy() {
    Validator.validateKMeansStrategy(KMeansStrategy.DEFAULT);
  }

  @Test
  public void testNullInitialClusterCentersAndInvalidNumberOfClusters() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateInitialClusterCentersAndNumberOfClusters(null,
        ClusterCenterInitializationStrategy.DEFAULT, 0));
  }

  @Test
  public void testValidInitialClusterCentersAndNumberOfClusters() {
    Validator.validateInitialClusterCentersAndNumberOfClusters(null, ClusterCenterInitializationStrategy.DEFAULT, 1);
  }

  @Test
  public void testValidNumberOfIterations() {
    Validator.validateNumberOfIterations(1);
  }

  @Test
  public void testNullClusterCentersAndValidDataPoints() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateClusterDataPointsAndClusterCenters(new double[][] { { 1 } }, null));
  }
}
