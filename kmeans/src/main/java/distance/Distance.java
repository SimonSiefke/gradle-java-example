package distance;

/**
 * Facade for all the Distance Strategies that are available.
 */
public final class Distance {
  public static final DistanceStrategy EUCLIDEAN = new EuclideanDistanceStrategy();

  private Distance() {
    // private constructor to prevent creating objects of this class.
  }
}
