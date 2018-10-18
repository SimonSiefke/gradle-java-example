package distance;

/**
 * Distance Context for choosing different strategies.
 */
public class DistanceContext {
  private DistanceStrategy strategy;

  public void setDistanceStrategy(DistanceStrategy strategy) {
    this.strategy = strategy;
  }

  public double getDistance(double[] pointA, double[] pointB) {
    return strategy.distance(pointA, pointB);
  }
}
