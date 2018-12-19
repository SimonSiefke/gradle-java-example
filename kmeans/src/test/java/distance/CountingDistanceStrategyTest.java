package distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CountingDistanceStrategyTest {
  @Test
  public void testZeroInvocations() {
    var strategy = new CountingDistanceStrategy(new EuclideanDistanceStrategy());
    assertEquals(strategy.numberOfDistanceCalculations, 0);
  }

  @Test
  public void testOneInvocation() {
    var strategy = new CountingDistanceStrategy(new EuclideanDistanceStrategy());
    strategy.compute(new double[] { 0.0 }, new double[] { 0.0 });
    assertEquals(strategy.numberOfDistanceCalculations, 1);
  }

  @Test
  public void testThreeInvocations() {
    var strategy = new CountingDistanceStrategy(new EuclideanDistanceStrategy());
    strategy.compute(new double[] { 0.0 }, new double[] { 0.0 });
    strategy.compute(new double[] { 0.0 }, new double[] { 0.0 });
    strategy.compute(new double[] { 0.0 }, new double[] { 0.0 });
    assertEquals(strategy.numberOfDistanceCalculations, 3);
  }
}
