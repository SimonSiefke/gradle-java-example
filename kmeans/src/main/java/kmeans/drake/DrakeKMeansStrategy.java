package kmeans.drake;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Drake's KMeans Strategy.
 */
public class DrakeKMeansStrategy implements KMeansStrategy {

  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    // TODO
    return new Cluster[0];
  }
}
