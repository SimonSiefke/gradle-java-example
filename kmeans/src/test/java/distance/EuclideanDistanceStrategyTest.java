package distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EuclideanDistanceStrategyTest extends DistanceStrategyTestBase<EuclideanDistanceStrategy> {
  @Override
  protected EuclideanDistanceStrategy createInstance() {
    return new EuclideanDistanceStrategy();
  }

  @Test
  public void testDifferentPoints() {
    var distance = new EuclideanDistanceStrategy().compute(new double[] { 0, 0 }, new double[] { 1, 1 });
    assertEquals(Math.sqrt(2), distance);
  }
}
