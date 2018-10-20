package kmeans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import util.Validator;

public class ValidatorTest {

  @Test
  public void testNullDataset() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateDataPoints(null));
  }

  @Test
  public void testEmptyDataset() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateDataPoints(new double[][] {}));
  }

  @Test
  public void testWrongDimensionsDataset1() {
    assertThrows(IllegalArgumentException.class, () -> Validator.validateDataPoints(new double[][] { { 1 }, {} }));
  }

  @Test
  public void testWrongDimensionsDataset2() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateDataPoints(new double[][] { { 1 }, { 2, 3 } }));
  }

  @Test
  public void testWrongDimensionsAndNullDataset() {
    assertThrows(IllegalArgumentException.class,
        () -> Validator.validateDataPoints(new double[][] { { 1 }, null, { 2, 3 } }));
  }

  @Test
  public void testCorrectDimensionsDataset() {
    Validator.validateDataPoints(new double[][] { { 0 }, { 0 } });
  }
}
