package kmeans.lloyd;

import java.util.Arrays;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Faster implementation of Lloyd's KMeans Strategy.
 */
public class FastLloydKMeansStrategy extends KMeansStrategy {
  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations,
      DistanceStrategy distance) {
    this.D = dataPoints[0].length;
    this.K = initialClusterCenters.length;
    this.N = dataPoints.length;

    this.clusterCenterMovements = new double[K];
    this.clusterCenters = Arrays.stream(initialClusterCenters).map(double[]::clone).toArray(double[][]::new);
    this.clusterSizes = new int[K];
    this.clusterSums = new double[K][D];
    this.dataPoints = dataPoints;
    this.dataPointsAssignments = new int[N];
    this.distance = distance;
    this.hasChanged = true;
    this.maxNumberOfIterations = maxNumberOfIterations;
    this.numberOfIterations = 0;

    initialize();
    main();
    return result();
  }

  @Override
  protected void initialize() {
    for (int n = 0; n < N; n++) {
      initialAssignPointToCluster(n, closestClusterCenterIndex(dataPoints[n]));
    }
  }

  @Override
  protected void loop() {
    for (int n = 0; n < N; n++) {
      assignPointToCluster(n, closestClusterCenterIndex(dataPoints[n]));
    }
    // move centers
    for (int k = 0; k < K; k++) {
      if (clusterSizes[k] > 0) {
        final double[] newClusterCenter = new double[D];
        for (int d = 0; d < D; d++) {
          newClusterCenter[d] = clusterSums[k][d] / clusterSizes[k];
          if (!hasChanged) {
            hasChanged = newClusterCenter[d] != clusterCenters[k][d];
          }
        }
        clusterCenters[k] = newClusterCenter;
      } else {
        throw new IllegalArgumentException(
            "Please provide different initial cluster centers, one or more of your initial clusters are too far away from any data point");
      }
    }
  }

  /**
   * @param dataPoint - the data point for which we want to compute the closest
   *                  cluster center index
   * @return the index of the cluster center that is closest to the given data
   *         point
   */
  private int closestClusterCenterIndex(double[] dataPoint) {
    int closestClusterCenterIndex = -1;
    double smallestClusterCenterDistance = Double.MAX_VALUE;

    for (int k = 0; k < K; k++) {
      double currentClusterCenterDistance = distance.compute(dataPoint, clusterCenters[k]);
      if (currentClusterCenterDistance < smallestClusterCenterDistance) {
        smallestClusterCenterDistance = currentClusterCenterDistance;
        closestClusterCenterIndex = k;
      }
    }
    return closestClusterCenterIndex;
  }
}
