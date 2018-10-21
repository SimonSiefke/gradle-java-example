package distance;

/**
 * Euclidean Distance Strategy.
 */
class EuclideanDistanceStrategy implements DistanceStrategy {
  @Override
  public double distance(double[] pointA, double[] pointB) {
    return Math.sqrt(new EuclideanSquaredDistanceStrategy().distance(pointA, pointB));
  }
}
