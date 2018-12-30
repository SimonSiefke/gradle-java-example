package distance;

/**
 * Manhattan Distance Strategy.
 */
public class ManhattanDistanceStrategy extends DistanceStrategy {
  @Override
  public double compute(double[] pointA, double[] pointB) {
    double sum = 0;
    for (int i = 0; i < pointA.length; i++) {
      sum += Math.abs(pointA[i] - pointB[i]);
    }
    return sum;
  }
}
