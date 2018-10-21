package distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EuclideanDistanceStrategyTest {
  @Test
  public void testSamePoint() {
    var distance = Distance.EUCLIDEAN(new double[] { 1, 1 }, new double[] { 1, 1 });
    assertEquals(0, distance);
  }

  @Test
  public void testDifferentPoints() {
    var distance = Distance.EUCLIDEAN(new double[] { 0, 0 }, new double[] { 1, 1 });
    assertEquals(Math.sqrt(2), distance);
  }
}
