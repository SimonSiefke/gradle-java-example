package distance;

/**
 * Euclidean Distance Strategy.
 */
public class EuclideanDistanceStrategy extends DistanceStrategy {
  private final EuclideanSquaredDistanceStrategy euclideanSquaredDistanceStrategy = new EuclideanSquaredDistanceStrategy();

  @Override
  public double compute(double[] pointA, double[] pointB) {
    return Math.sqrt(this.euclideanSquaredDistanceStrategy.compute(pointA, pointB));
  }
}
