package distance;

/**
 * Facade for all the Distance Strategies that are available.
 */
public final class DistanceStrategies {
  public static final DistanceStrategy EUCLIDEAN = new EuclideanDistanceStrategy();

  private DistanceStrategies() {
    // private constructor to prevent creating objects of this class.
  }
}
