package distance;

/**
 * Facade for all the Distance Strategies that are available.
 */
public final class Distance {
  private static final DistanceStrategy EUCLIDEAN = new EuclideanDistanceStrategy();
  private static final DistanceStrategy EUCLIDEAN_SQUARED = new EuclideanSquaredDistanceStrategy();

  public static double DEFAULT(double[] pointA, double[] pointB) {
    return EUCLIDEAN_SQUARED.distance(pointA, pointB);
    // return EUCLIDEAN.distance(pointA, pointB);

  }

  public static double EUCLIDEAN(double[] pointA, double[] pointB) {
    return EUCLIDEAN.distance(pointA, pointB);
  }

  public static double EUCLIDEAN_SQUARED(double[] pointA, double[] pointB) {
    return EUCLIDEAN_SQUARED.distance(pointA, pointB);
  }

  private Distance() {
    // private constructor to prevent creating objects of this class.
  }
}
