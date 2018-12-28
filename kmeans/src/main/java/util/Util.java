package util;

/**
 * Utility class for KMeans methods.
 */
public final class Util {
  /**
   * Assigns a point to a cluster.
   *
   * @param dataPointsAssignments       - the cluster to which each point is
   *                                    assigned
   * @param n                           - the data point index (the old data
   *                                    points assignment index is
   *                                    dataPointsAssignments[n])
   * @param newDataPointAssignmentIndex - the new index of the data point to which
   *                                    the cluster is assigned
   * @param clusterSizes                - the sizes of every cluster
   * @param clusterSums                 - the sum of the points assigned to each
   *                                    cluster center (for each dimension).
   * @param D                           - the number of dimensions
   * @param dataPoints                  - the data points
   */
  public static void assignPointToCluster(int[] dataPointsAssignments, int n, int newDataPointAssignmentIndex,
      int[] clusterSizes, double[][] clusterSums, int D, double[][] dataPoints) {
    if (dataPointsAssignments[n] != newDataPointAssignmentIndex) {
      clusterSizes[dataPointsAssignments[n]]--;
      clusterSizes[newDataPointAssignmentIndex]++;
      for (int d = 0; d < D; d++) {
        clusterSums[dataPointsAssignments[n]][d] -= dataPoints[n][d];
        clusterSums[newDataPointAssignmentIndex][d] += dataPoints[n][d];
      }
      dataPointsAssignments[n] = newDataPointAssignmentIndex;
    }
  }

  private Util() {
    // private constructor to prevent creating objects of this class.
  }
}
