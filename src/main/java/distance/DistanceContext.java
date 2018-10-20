package distance;

import context.Context;
import javax.annotation.Nonnull;

/**
 * Distance Context for choosing different strategies.
 */
public class DistanceContext extends Context<DistanceStrategy> {
  public double getDistance(@Nonnull double[] pointA, @Nonnull double[] pointB) {
    return strategy.distance(pointA, pointB);
  }
}
