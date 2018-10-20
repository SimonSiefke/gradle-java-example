package distance;

import context.Context;

/**
 * Distance Context for choosing different strategies.
 */
public class DistanceContext extends Context<DistanceStrategy> {
  public double getDistance(double[] pointA, double[] pointB) {
    return strategy.distance(pointA, pointB);
  }
}
