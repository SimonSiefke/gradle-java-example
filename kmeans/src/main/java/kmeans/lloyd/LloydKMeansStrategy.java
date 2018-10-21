package kmeans.lloyd;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import distance.Distance;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Validator;

/**
 * Lloyd KMeans Strategy.
 */
public class LloydKMeansStrategy implements KMeansStrategy {
  private Cluster nearestCluster(@Nonnull double[] point, @Nonnull Cluster[] clusters) {
    Cluster nearestCluster = null;
    double minDistance = Double.MAX_VALUE;
    for (var cluster : clusters) {
      var currentDistance = Distance.EUCLIDEAN(point, cluster.center);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
        nearestCluster = cluster;
      }
    }
    return nearestCluster;
  }

  private double[] average(List<double[]> points) {
    if (points.isEmpty()) {
      return null;
    }
    final double[] sum = new double[points.get(0).length];
    for (var point : points) {
      for (int dimension = 0; dimension < point.length; dimension++) {
        var coordinateValue = point[dimension];
        sum[dimension] += coordinateValue;
      }
    }
    for (int i = 0; i < sum.length; i++) {
      sum[i] /= points.size();
    }
    return sum;
  }

  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      @Nonnull int maxNumberOfIterations) {
    Validator.validateDataPoints(dataPoints);
    Validator.validateClusterCenters(initialClusterCenters);
    Validator.validateNumberOfIterations(maxNumberOfIterations);

    int numberOfIterations = 0;
    boolean hasChanged = true;

    final Cluster[] clusters = Arrays.stream(initialClusterCenters).map(Cluster::new).toArray(Cluster[]::new);

    while (numberOfIterations < maxNumberOfIterations && hasChanged) {
      // step 0: clean up points in each cluster because they are recalculated in step
      // 1
      for (var cluster : clusters) {
        cluster.closestPoints.clear();
      }

      // step 1: assign each point to its nearest cluster
      for (var point : dataPoints) {
        var nearestCluster = nearestCluster(point, clusters);
        if (nearestCluster != null) {
          nearestCluster.closestPoints.add(point);
        }
      }

      // step 2: assign each cluster to the average of its points
      for (var cluster : clusters) {
        if (cluster.closestPoints.size() == 0) {
          continue;
        }
        cluster.center = average(cluster.closestPoints);
      }
      numberOfIterations++;
    }
    return clusters;
  }
}
