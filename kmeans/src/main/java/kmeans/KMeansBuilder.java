package kmeans;

import java.util.Arrays;

import clusterCenterInitialization.CLUSTER_CENTER_INITIALIZATION_STRATEGY;
import clusterCenterInitialization.ClusterCenterInitializationStrategy;
import clusterCenterInitialization.FirstKClusterCenterInitializationStrategy;
import clusterCenterInitialization.KPlusPlusClusterCenterInitializationStrategy;
import distance.DISTANCE_STRATEGY;
import distance.DistanceStrategy;
import distance.EuclideanDistanceStrategy;
import distance.EuclideanSquaredDistanceStrategy;
import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;
import util.Validator;

import javax.annotation.Nonnull;

/**
 * Builder for all the KMeans Strategies that are available.
 */
public final class KMeansBuilder {
  private ClusterCenterInitializationStrategy clusterCenterInitializationStrategy;
  private double[][] dataPoints;
  private DistanceStrategy distanceStrategy;
  private double[][] initialClusterCenters;
  private KMeansStrategy kMeansStrategy;
  private int maxNumberOfIterations;
  private int numberOfClusters;

  public KMeansBuilder() {
    this.withClusterCenterInitializationStrategy(CLUSTER_CENTER_INITIALIZATION_STRATEGY.DEFAULT);
    this.withDistance(DISTANCE_STRATEGY.DEFAULT);
    this.withStrategy(KMEANS_STRATEGY.DEFAULT);
    this.withMaxNumberOfIterations(Integer.MAX_VALUE);
  }

  /**
   * Clones a k-means builder.
   *
   * @param original the original
   */
  private KMeansBuilder(@Nonnull KMeansBuilder original) {
    // TODO test if copy is deep
    this.clusterCenterInitializationStrategy = original.clusterCenterInitializationStrategy;
    if (original.dataPoints != null) {
      this.dataPoints = new double[original.dataPoints.length][];
      if (original.dataPoints.length > 0) {
        for (var x = 0; x < original.dataPoints.length; x++) {
          this.dataPoints[x] = Arrays.copyOf(original.dataPoints[x], original.dataPoints[0].length);
        }
      }
    }
    this.distanceStrategy = original.distanceStrategy;
    if (original.initialClusterCenters != null) {
      this.initialClusterCenters = new double[original.initialClusterCenters.length][];
      if (original.initialClusterCenters.length > 0) {
        for (var x = 0; x < original.initialClusterCenters.length; x++) {
          this.initialClusterCenters[x] = Arrays.copyOf(original.initialClusterCenters[x],
              original.initialClusterCenters[0].length);
        }
      }
    }
    this.kMeansStrategy = original.kMeansStrategy;
    this.maxNumberOfIterations = original.maxNumberOfIterations;
    this.numberOfClusters = original.numberOfClusters;
  }

  /**
   * Sets the data points that should be clustered
   *
   * @param dataPoints the data points
   */
  public KMeansBuilder withDataPoints(double[][] dataPoints) {
    this.dataPoints = dataPoints;
    return new KMeansBuilder(this);
  }

  /**
   * Sets the distance measurement method
   *
   * @param distanceMethod the distance measurement method
   */
  public KMeansBuilder withDistance(DISTANCE_STRATEGY distanceMethod) {
    switch (distanceMethod) {
    case EUCLIDEAN:
    case DEFAULT:
      this.distanceStrategy = new EuclideanDistanceStrategy();
      break;
    case EUCLIDEAN_SQUARED:
      this.distanceStrategy = new EuclideanSquaredDistanceStrategy();
    default:
      break;
    }
    return new KMeansBuilder(this);
  }

  /**
   * Sets the cluster center initialization strategy. initialization
   *
   * @param clusterCenterInitializationStrategy the cluster center initialization
   *                                            strategy
   */
  public KMeansBuilder withClusterCenterInitializationStrategy(
      CLUSTER_CENTER_INITIALIZATION_STRATEGY clusterCenterInitializationStrategy) {
    switch (clusterCenterInitializationStrategy) {
    case FIRST_K:
    case DEFAULT:
      this.clusterCenterInitializationStrategy = new FirstKClusterCenterInitializationStrategy();
      break;
    case K_PLUS_PLUS:
      this.clusterCenterInitializationStrategy = new KPlusPlusClusterCenterInitializationStrategy();
      break;
    default:
      break;
    }
    return new KMeansBuilder(this);
  }

  /**
   * Sets the maximum number of iterations. The algorithm may converge before the
   * maximum number of iterations is reached.
   *
   * @param maxNumberOfIterations the maximum number of iterations
   */
  public KMeansBuilder withMaxNumberOfIterations(int maxNumberOfIterations) {
    this.maxNumberOfIterations = maxNumberOfIterations;
    return new KMeansBuilder(this);
  }

  /**
   * Sets the initial clusters centers. You can also use the
   * {@link #numberOfClusters} method instead.
   *
   * @param initialClusterCenters the initial cluster centers to set
   */
  public KMeansBuilder withInitialClusterCenters( @Nonnull double[][] initialClusterCenters) {
    this.initialClusterCenters = initialClusterCenters;
    return new KMeansBuilder(this);
  }

  /**
   * Sets the number of clusters. The initial clusters will be calculated by
   * aAlternatively {@link #clusterCenterInitializationStrategy}. Alternatively
   * you can set the initial clusters directly with
   * {@link #initialClusterCenters}.
   */
  public KMeansBuilder withNumberOfClusters(int numberOfClusters) {
    this.numberOfClusters = numberOfClusters;
    return new KMeansBuilder(this);
  }

  /**
   * Sets the k-means algorithm which should be used for clustering (e.g.
   * {@link LloydKMeansStrategy} or {@link ElkanKMeansStrategy})
   *
   * @param kMeansStrategy the k-means algorithm to use
   */
  public KMeansBuilder withStrategy(KMEANS_STRATEGY kMeansStrategy) {
    switch (kMeansStrategy) {
    case LLOYD:
    case DEFAULT:
      this.kMeansStrategy = new LloydKMeansStrategy();
      break;
    case ELKAN:
      this.kMeansStrategy = new ElkanKMeansStrategy();
      break;
    default:
      break;
    }
    return new KMeansBuilder(this);
  }

  /**
   * Clusters the data.
   *
   * @return the clusters
   */
  public Cluster[] cluster() {
    Validator.validateInitialClusterCentersAndNumberOfClusters(this.initialClusterCenters,
        this.clusterCenterInitializationStrategy, this.numberOfClusters);
    if (this.initialClusterCenters == null) {
      this.initialClusterCenters = this.clusterCenterInitializationStrategy.initialize(this.dataPoints,
          this.numberOfClusters, this.distanceStrategy);
    }
    Validator.validateClusterDataPointsAndClusterCenters(this.dataPoints, this.initialClusterCenters);
    Validator.validateNumberOfIterations(this.maxNumberOfIterations);
    Validator.validateKMeansStrategy(this.kMeansStrategy);
    return this.kMeansStrategy.cluster(dataPoints, initialClusterCenters, this.maxNumberOfIterations,
        this.distanceStrategy);
  }
}