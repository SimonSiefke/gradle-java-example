package kmeans;

import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;

/**
 * Facade for all the Distance Strategies that are available.
 */
public final class KMeans {
  public static final KMeansStrategy LLOYD = new LloydKMeansStrategy();
  public static final KMeansStrategy ELKAN = new ElkanKMeansStrategy();

  private KMeans() {
    // private constructor to prevent creating objects of this class.
  }
}
