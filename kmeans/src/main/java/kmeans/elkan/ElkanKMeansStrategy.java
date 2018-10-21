package kmeans.elkan;

import javax.annotation.Nonnull;

import distance.Distance;
import kmeans.Cluster;
import kmeans.KMeansStrategy;

/**
 * Elkan KMeans Strategy.
 */
public class ElkanKMeansStrategy implements KMeansStrategy {
  @Override
  public Cluster[] cluster(double[][] dataPoints, double[][] initialClusterCenters, int maxNumberOfIterations) {
    // // TODO see
    // //
    // https://www.programcreek.com/java-api-examples/index.php?source_dir=JSAT-master/JSAT/src/jsat/clustering/kmeans/ElkanKernelKMeans.java
    // final int N = dataPoints.length;
    // final int k = initialClusterCenters.length;

    // final double[][] lowerBound = new double[N][k];
    // final double[] upperBound = new double[N];

    // // keeps track of the distances between all of the clusters
    // final double[][] interClusterDistances = new double[k][k];
    // final double[] sC = new double[k];

    // int numberOfIterations = 0;
    // boolean hasChanged = true;

    // final ElkanCluster[] clusters =
    // Arrays.stream(initialClusterCenters).map(ElkanCluster::new)
    // .toArray(ElkanCluster[]::new);

    // // step 1: set all lower bounds to 0
    // double[][] l = new double[dataPoints.length][clusters.length];
    // for (int i = 0; i < l.length; i++) {
    // for (int j = 0; j < l[0].length; j++) {
    // l[i][j] = 0;
    // }
    // }

    // // step 2: assign each point to its nearest cluster
    // for (var point : dataPoints) {
    // var closestCluster = closestCluster(point, clusters);
    // closestCluster.closestPoints.add(point);
    // }

    // // TODO: hashmap or array?
    // var ux = new double[dataPoints.length];
    // // TODO compute upper bounds

    // while (numberOfIterations < maxNumberOfIterations && hasChanged) {
    // var dccArr = new double[clusters.length][clusters.length];
    // var scArr = new double[clusters.length];
    // var rx = new boolean[dataPoints.length];

    // // 1
    // for (int i = 0; i < clusters.length; i++) {
    // var dccmin = Double.MAX_VALUE;
    // for (int j = 0; j < clusters.length; j++) {
    // var dcc = Distance.EUCLIDEAN(clusters[i].center, clusters[j].center);
    // dccArr[i][j] = dcc;
    // if (i != j) {
    // var sc = Math.min(dccmin, 0.5 * dcc);
    // scArr[i] = sc;
    // }
    // }
    // }

    // // 3
    // for (int i = 0; i < dataPoints.length; i++) {
    // // TODO
    // int centerIndex = 0;
    // if (ux[i] <= scArr[centerIndex]) {
    // // check 1
    // // check 2
    // // check 3
    // if (rx[i]) {
    // dccArr[i][i] = Distance.EUCLIDEAN(dataPoints[i],
    // clusters[centerIndex].center);
    // rx[i] = false;
    // } else {
    // dccArr[i][i] = ux[i];
    // }
    // }
    // }

    // // 4
    // double[][] mcArr = new double[clusters.length][clusters[0].center.length];
    // for (int i = 0; i < mcArr.length; i++) {
    // mcArr[i] = Util.averageOfPoints(clusters[i].closestPoints);
    // }

    // // 5
    // for (int i = 0; i < dataPoints.length; i++) {
    // for (int j = 0; j < clusters.length; j++) {
    // l[i][j] = Math.max(l[i][j] - Distance.EUCLIDEAN(clusters[j].center,
    // mcArr[j]), 0);
    // }
    // }

    // // 6
    // for (int i = 0; i < dataPoints.length; i++) {
    // // TODO
    // var centerIndex = 0;
    // ux[i] += Distance.EUCLIDEAN(mcArr[centerIndex],
    // clusters[centerIndex].center);
    // }

    // // 7
    // for (int i = 0; i < clusters.length; i++) {
    // clusters[i].center = mcArr[i];
    // }

    // numberOfIterations++;
    // }
    return new Cluster[] {};
  }

  // TODO use triangle inequality of lemma 1
  // TODO upper bounds
  private ElkanCluster closestCluster(@Nonnull double[] point, @Nonnull ElkanCluster[] clusters) {
    ElkanCluster closestCluster = null;
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
