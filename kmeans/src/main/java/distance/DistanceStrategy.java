package distance;

import javax.annotation.Nonnull;

/**
 * Distance method interface;
 */
public interface DistanceStrategy {
  /**
   * Computes a distance between the two points.
   *
   * @param pointA the first point
   * @param pointB the second point
   * @return the distance between the two points
   */
  double compute(@Nonnull double[] pointA, @Nonnull double[] pointB);
}
