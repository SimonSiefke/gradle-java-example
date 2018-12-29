package distance;

import javax.annotation.Nonnull;

/**
 * Euclidean Squared Distance Strategy.
 */
public class EuclideanSquaredDistanceStrategy extends DistanceStrategy {
  @Override
  public double compute(@Nonnull double[] pointA, @Nonnull double[] pointB) {
    double squaredSum = 0;
    for (int i = 0; i < pointA.length; i++) {
      squaredSum += Math.pow((pointA[i] - pointB[i]), 2);
    }
    return squaredSum;
  }
}
