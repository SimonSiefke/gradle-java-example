package distance;

/**
 * Abstract class for Distance Measurement Strategies.
 */
public abstract class DistanceStrategy {
  public static final DistanceStrategy DEFAULT = new EuclideanSquaredDistanceStrategy();
  public static final DistanceStrategy EUCLIDEAN = new EuclideanSquaredDistanceStrategy();
  public static final DistanceStrategy EUCLIDEAN_SQUARED = new EuclideanDistanceStrategy();
  public static final DistanceStrategy MANHATTAN = new ManhattanDistanceStrategy();

  /**
   * Computes a distance between the two points.
   *
   * @param pointA the first point
   * @param pointB the second point
   * @return the distance between the two points
   */
  public abstract double compute(double[] pointA, double[] pointB);
}
