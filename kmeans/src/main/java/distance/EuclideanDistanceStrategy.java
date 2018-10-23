package distance;

import java.util.function.ToDoubleBiFunction;

/**
 * Euclidean Distance Strategy.
 */
public class EuclideanDistanceStrategy implements DistanceStrategy {
  private final EuclideanSquaredDistanceStrategy euclideanSquaredDistanceStrategy = new EuclideanSquaredDistanceStrategy();

  @Override
  public double compute(double[] pointA, double[] pointB) {
    return Math.sqrt(this.euclideanSquaredDistanceStrategy.compute(pointA, pointB));
  }
}
