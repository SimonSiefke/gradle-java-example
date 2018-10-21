package kmeans;

import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;
import util.Validator;

/**
 * Facade for all the KMeans Strategies that are available.
 */
public final class KMeans {
  private static final KMeansStrategy LLOYD = new LloydKMeansStrategy();
  private static final KMeansStrategy ELKAN = new ElkanKMeansStrategy();

  public static Cluster[] LLOYD(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations) {
    Validator.validateDataPoints(dataPoints);
    Validator.validateClusterCenters(initialClusterCenters);
    Validator.validateNumberOfIterations(maxNumberOfIterations);
    return LLOYD.cluster(dataPoints, initialClusterCenters, maxNumberOfIterations);
  }

  public static Cluster[] ELKAN(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations) {
    Validator.validateDataPoints(dataPoints);
    Validator.validateClusterCenters(initialClusterCenters);
    Validator.validateNumberOfIterations(maxNumberOfIterations);
    return ELKAN.cluster(dataPoints, initialClusterCenters, maxNumberOfIterations);
  }

  private KMeans() {
    // private constructor to prevent creating objects of this class.
  }
}
