package clusterCenterInitialization;

/**
 * Facade for all the Cluster Center Initialization Strategies that are
 * available.
 */
public final class ClusterCenterInitialization {
  private static final ClusterCenterInitializationStrategy FIRST_K = new FirstKClusterCenterInitializationStrategy();

  public static double[][] DEFAULT(double[][] dataPoints, int k) {
    return FIRST_K.initialize(dataPoints, k);
  }

  public static double[][] FIRST_K(double[][] dataPoints, int k) {
    return FIRST_K.initialize(dataPoints, k);
  }

  private ClusterCenterInitialization() {
    // private constructor to prevent creating objects of this class.
  }
}
