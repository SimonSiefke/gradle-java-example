package distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EuclideanSquaredDistanceStrategyTest extends DistanceStrategyTestBase<EuclideanSquaredDistanceStrategy> {
  @Override
  protected EuclideanSquaredDistanceStrategy createInstance() {
    return new EuclideanSquaredDistanceStrategy();
  }

  @Test
  public void testDifferentPoints() {
    var distance = new EuclideanSquaredDistanceStrategy().compute(new double[] { 0, 0 }, new double[] { 1, 1 });
    assertEquals(2, distance);
  }
}
