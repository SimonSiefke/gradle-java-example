package distance;

/**
 * Interface for Distance Strategies.
 */
public interface DistanceStrategy {
  /**
   * computes a distance between the two points.
   *
   * @param pointA the first point
   * @param pointB the second point
   * @return the distance between the two points
   */
  double distance(double[] pointA, double[] pointB);
}
