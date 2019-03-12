package distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ManhattanDistanceStrategyTest extends DistanceStrategyTestBase<ManhattanDistanceStrategy> {
  @Override
  protected ManhattanDistanceStrategy createInstance() {
    return new ManhattanDistanceStrategy();
  }

  @Test
  public void testDifferentPoints() {
    var distance = new ManhattanDistanceStrategy().compute(new double[] { 0, 0 }, new double[] { 1, 1 });
    assertEquals(2, distance);
  }
}
