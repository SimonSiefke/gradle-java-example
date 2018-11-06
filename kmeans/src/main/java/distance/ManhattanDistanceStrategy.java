package distance;

import javax.annotation.Nonnull;

/**
 * Manhattan Distance Strategy.
 */
public class ManhattanDistanceStrategy implements DistanceStrategy {
  @Override
  public double compute(@Nonnull double[] pointA, @Nonnull double[] pointB) {
    double sum = 0;
    for (int i = 0; i < pointA.length; i++) {
      sum += Math.abs(pointA[i] - pointB[i]);
    }
    return sum;
  }
}
