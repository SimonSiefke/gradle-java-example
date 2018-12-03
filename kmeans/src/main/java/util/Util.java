package util;

/**
 * Utility class for KMeans methods.
 */
public final class Util {
  /**
   * Assigns a point to a cluster.
   *
   * @param clusterAssignments   - the cluster assignments
   * @param n                    - the data point index (the old cluster
   *                             assignment index is clusterAssignments[n])
   * @param newClusterAssignment - the new cluster assignment index
   * @param clusterSizes         - the sizes of every cluster
   * @param clusterSums          - the sum of the points assigned to each cluster
   *                             center (for each dimension).
   * @param D                    - the number of dimensions
   * @param dataPoints           - the data points
   */
  public static void assignPointToCluster(int[] clusterAssignments, int n, int newClusterAssignment, int[] clusterSizes,
      double[][] clusterSums, int D, double[][] dataPoints) {
    if (clusterAssignments[n] != newClusterAssignment) {
      clusterSizes[clusterAssignments[n]]--;
      clusterSizes[newClusterAssignment]++;
      for (int d = 0; d < D; d++) {
        clusterSums[clusterAssignments[n]][d] -= dataPoints[n][d];
        clusterSums[newClusterAssignment][d] += dataPoints[n][d];
      }
      clusterAssignments[n] = newClusterAssignment;
    }
  }

  private Util() {
    // private constructor to prevent creating objects of this class.
  }
}
