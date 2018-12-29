package distance;

import javax.annotation.Nonnull;

/**
 * Euclidean Distance Strategy.
 */
public class EuclideanDistanceStrategy extends DistanceStrategy {
  private final EuclideanSquaredDistanceStrategy euclideanSquaredDistanceStrategy = new EuclideanSquaredDistanceStrategy();

  @Override
  public double compute(@Nonnull double[] pointA, @Nonnull double[] pointB) {
    return Math.sqrt(this.euclideanSquaredDistanceStrategy.compute(pointA, pointB));
  }
}
