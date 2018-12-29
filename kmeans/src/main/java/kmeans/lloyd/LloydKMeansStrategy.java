package kmeans.lloyd;

import java.util.Arrays;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import distance.DistanceStrategy;
import kmeans.Cluster;
import kmeans.KMeansStrategy;
import util.Util;

/**
 * Lloyd's KMeans Strategy.
 */
public class LloydKMeansStrategy extends KMeansStrategy {
  @Override
  public Cluster[] cluster(@Nonnull double[][] dataPoints, @Nonnull double[][] initialClusterCenters,
      int maxNumberOfIterations, @Nonnull DistanceStrategy distance) {
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
    moveCenters();
    numberOfIterations++;
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
