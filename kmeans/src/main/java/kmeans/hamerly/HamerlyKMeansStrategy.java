package kmeans.hamerly;

import java.util.Arrays;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Hamerly KMeans Strategy.
 */
public class HamerlyKMeansStrategy implements KMeansStrategy {
  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      @Nonnull int maxNumberOfIterations, @Nonnull DistanceStrategy distance) {
    // TODO
    return new Cluster[0];
  }
}
