package kmeans;

/**
 * Facade for all the Distance Strategies that are available.
 */
public final class KMeansStrategies {
  public static final KMeansStrategy LLOYD = new LloydKMeansStrategy();
  public static final KMeansStrategy ELKAN = new ElkanKMeansStrategy();

  private KMeansStrategies() {
    // private constructor to prevent creating objects of this class.
  }
}
