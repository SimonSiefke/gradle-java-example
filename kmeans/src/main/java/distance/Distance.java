package distance;

/**
 * Facade for all the Distance Strategies that are available.
 */
public final class Distance {
  private static final DistanceStrategy EUCLIDEAN = new EuclideanDistanceStrategy();

  public static double EUCLIDEAN(double[] pointA, double[] pointB) {
    return EUCLIDEAN.distance(pointA, pointB);
  }

  private Distance() {
    // private constructor to prevent creating objects of this class.
  }
}
