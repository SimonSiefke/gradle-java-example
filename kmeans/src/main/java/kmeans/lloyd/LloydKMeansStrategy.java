package kmeans.lloyd;

import java.util.Arrays;

import javax.annotation.Nonnull;

import distance.Distance;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Lloyd KMeans Strategy.
 */
public class LloydKMeansStrategy implements KMeansStrategy {
  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      @Nonnull int maxNumberOfIterations) {
    int numberOfIterations = 0;
    boolean hasChanged = true;

    final Cluster[] clusters = Arrays.stream(initialClusterCenters).map(Cluster::new).toArray(Cluster[]::new);

    while (numberOfIterations < maxNumberOfIterations && hasChanged) {
      // step 0: clean up points in each cluster
      // because they are recalculated in step 1
      for (var cluster : clusters) {
        cluster.closestPoints.clear();
      }

      // step 1: assign each point to its nearest cluster
      for (var point : dataPoints) {
        var closestCluster = closestCluster(point, clusters);
        closestCluster.closestPoints.add(point);
      }

      // step 2: assign each cluster to the average of its points
      for (var cluster : clusters) {
        if (cluster.closestPoints.size() == 0) {
          continue;
        }
        cluster.center = Util.averageOfPoints(cluster.closestPoints);
      }
      numberOfIterations++;
    }
    return clusters;
  }

  private Cluster closestCluster(@Nonnull double[] point, @Nonnull Cluster[] clusters) {
    Cluster closestCluster = null;
    double minDistance = Double.MAX_VALUE;
    for (var cluster : clusters) {
      var currentDistance = Distance.EUCLIDEAN(point, cluster.center);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
        closestCluster = cluster;
      }
    }
    return closestCluster;
  }
}
