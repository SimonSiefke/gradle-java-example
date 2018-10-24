package kmeans.lloyd;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Lloyd KMeans Strategy.
 */
public class LloydKMeansStrategy implements KMeansStrategy {
  private DistanceStrategy distance;

  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
       int maxNumberOfIterations, @Nonnull DistanceStrategy distance) {
    this.distance = distance;
    int numberOfIterations = 0;
    boolean hasChanged = true;

    final Cluster[] clusters = Arrays.stream(initialClusterCenters).map(Cluster::new).toArray(Cluster[]::new);

    while (hasChanged && numberOfIterations < maxNumberOfIterations) {
      hasChanged = false;

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
        var currentCenter = cluster.center;
        var newCenter = Util.averageOfPoints(cluster.closestPoints);
        cluster.center = newCenter;
        if (this.distance.compute(currentCenter, newCenter) > 0) {
          hasChanged = true;
        }
      }
      numberOfIterations++;
    }
    return clusters;
  }

  /**
   * Computes the cluster that is closest to a given point.
   *
   * @param point    the point for which we want the closest cluster
   * @param clusters all the clusters
   * @return the closest cluster
   */
  private Cluster closestCluster(@Nonnull double[] point, @Nonnull Cluster[] clusters) {
    Cluster closestCluster = null;
    double minDistance = Double.MAX_VALUE;
    for (var cluster : clusters) {
      var currentDistance = this.distance.compute(point, cluster.center);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
        closestCluster = cluster;
      }
    }
    return closestCluster;
  }
}
