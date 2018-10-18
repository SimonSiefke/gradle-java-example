package distance;

/**
 * Euclidean Distance Strategy.
 */
public class EuclideanDistanceStrategy implements DistanceStrategy {
  @Override
  public double distance(double[] pointA, double[] pointB) {
    double squaredSum = 0;

    for (int i = 0; i < pointA.length; i++) {
      squaredSum += (pointA[i] - pointB[i]) * (pointA[i] - pointB[i]);
    }

    return Math.sqrt(squaredSum);
  }
}
