package kmeans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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

  // @ParameterizedTest
  // @CsvSource({ "{1,2}, 5", "JUnit 5, 7", "'Hello, JUnit 5!', 15" })
  // public void testWrongDimensionsDataset(Object x) {
  // System.out.println(x);
  // assertThrows(IllegalArgumentException.class, () ->
  // Validator.validateDataPoints(new double[][] {}));
  // }

  // @ParameterizedTest
  // @CsvSource({ "0, 0, 0", "1, 0, 1", "1.414, 1, 1" })
  // void testPointNorm(double norm, @AggregateWith(PointAggregator.class) Point
  // point) {
  // }

  // static class PointAggregator implements ArgumentsAggregator {

  // @Override
  // public Object aggregateArguments(ArgumentsAccessor arguments,
  // ParameterContext context)
  // throws ArgumentsAggregationException {

  // // step 1: convert Object[] to Double[]
  // Double[] coordinates =
  // arguments.toList().stream().map(Object::toString).map(Double::parseDouble)
  // .toArray(Double[]::new);

  // // step 2: convert Double[] to double[]
  // double[] doubleCoordinates =
  // Stream.of(coordinates).mapToDouble(Double::doubleValue).toArray();

  // return new Point(doubleCoordinates);
  // }
  // }
}
