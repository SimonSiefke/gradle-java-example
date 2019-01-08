package distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class DistanceStrategyTestBase<T extends DistanceStrategy> {

  private T instance;

  protected abstract T createInstance();

  @BeforeEach
  public void setUp() {
    instance = createInstance();
  }

  @Test
  public void testSamePoint() {
    var distance = instance.compute(new double[] { 1, 1 }, new double[] { 1, 1 });
    assertEquals(0, distance);
  }

  @Test
  public void testSamePointZero() {
    var distance = instance.compute(new double[] { 0 }, new double[] { 0 });
    assertEquals(0, distance);
  }
}
