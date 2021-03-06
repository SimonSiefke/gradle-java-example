package distance;

/**
 * Counting Distance Strategy. A utility class that Keeps track of how many
 * distance calculations were made.
 */
public class CountingDistanceStrategy extends DistanceStrategy {
  private final DistanceStrategy distanceStrategy;
  public int numberOfDistanceCalculations = 0;

  public CountingDistanceStrategy(DistanceStrategy distanceStrategy) {
    this.distanceStrategy = distanceStrategy;
  }

  @Override
  public double compute(double[] pointA, double[] pointB) {
    this.numberOfDistanceCalculations++;
    return this.distanceStrategy.compute(pointA, pointB);
  }
}
