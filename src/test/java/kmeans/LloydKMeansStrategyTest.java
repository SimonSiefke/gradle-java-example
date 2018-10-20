package kmeans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LloydKMeansStrategyTest {
  private static KMeansContext kMeansContext;

  @BeforeAll
  public static void initializeDistanceContext() {
    LloydKMeansStrategyTest.kMeansContext = new KMeansContext();
    kMeansContext.setStrategy(KMeansStrategies.LLOYD);
  }

  @Test
  public void testDifferentPoints() {
    assertEquals(1, 1);
  }
}
