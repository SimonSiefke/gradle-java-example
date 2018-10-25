package util;

/**
 * Utility class for KMeans methods.
 */
public final class Util {
  /**
   * updates the cluster centers to the mean of the points that are assigned to
   * each cluster.
   *
   * @param originalClusterCenters the original cluster centers
   * @param copyToClusterCenters   the cluster centers that will be updated based
   *                               on the values by the original cluster centers.
   *                               OriginalClusterCenters and copyToClusterCenters
   *                               can be the same.
   * @param clusterAssignments     indices of which point is assigned to which
   *                               cluster
   * @param dataPoints             the data points
   * @return if one or more of the clusters have changed
   */
  public static boolean updateClusterCenters(double[][] originalClusterCenters, double[][] copyToClusterCenters,
      int[] clusterAssignments, double[][] dataPoints) {
    final int K = originalClusterCenters.length;
    final int N = dataPoints.length;
    final int D = dataPoints[0].length;
    final double[][] clusterSums = new double[K][D];
    boolean hasChanged = false;
    final int[] numberOfPointsInClusters = new int[K];

    for (int n = 0; n < N; n++) {
      numberOfPointsInClusters[clusterAssignments[n]]++;
      for (int d = 0; d < D; d++) {
        clusterSums[clusterAssignments[n]][d] += dataPoints[n][d];
      }
    }

    for (int k = 0; k < K; k++) {
      if (numberOfPointsInClusters[k] > 0) {
        for (int d = 0; d < D; d++) {
          double newClusterCenterCoordinate = clusterSums[k][d] / numberOfPointsInClusters[k];
          hasChanged = originalClusterCenters[k][d] != newClusterCenterCoordinate;
          copyToClusterCenters[k][d] = newClusterCenterCoordinate;
        }
      } else {
        copyToClusterCenters[k] = originalClusterCenters[k];
      }
    }
    return hasChanged;
  }

  private Util() {
    // private constructor to prevent creating objects of this class.
  }
}
