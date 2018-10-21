package kmeans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import util.Validator;

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
  public void testWrongNumberOfIterations() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateNumberOfIterations(-1));
  }
}
