import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import util.Point;

public class PointTest {
  @Test
  public void testPoint() {
    Point point = new Point(1, 2);
    assertArrayEquals(new double[] { 1, 2 }, point.coordinates);
  }
}
