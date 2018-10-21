package util;

/**
 * Validator for validating all kinds of stuff.
 */
public final class Validator {
  public static void validateClusterDataPointsAndClusterCenters(double[][] dataPoints, double[][] clusterCenters) {
    Validator.validateDataPoints(dataPoints);
    Validator.validateClusterCenters(clusterCenters);
    if (dataPoints.length < clusterCenters.length) {
      throw new IllegalArgumentException("there are more cluster centers than data points");
    }
    if (dataPoints[0].length != clusterCenters[0].length) {
      throw new IllegalArgumentException("data points and cluster centers must have the same dimension");
    }
  }

  public static void validateNumberOfIterations(int numberOfIterations) {
    if (numberOfIterations <= 0) {
      throw new IllegalArgumentException("number of iterations must be greater than 0");
    }
  }

  private static void validateDataPoints(double[][] dataPoints) {
    if (dataPoints == null) {
      throw new IllegalArgumentException("Data set is null.");
    } else if (dataPoints.length == 0) {
      throw new IllegalArgumentException("Data set is empty.");
    } else if (dataPoints[0] == null) {
      throw new IllegalArgumentException("Bad data set format.");
    }
    final int dimension = dataPoints[0].length;
    for (var point : dataPoints) {
      if (point == null || point.length != dimension) {
        throw new IllegalArgumentException("Bad data set format.");
      }
    }
  }

  private static void validateClusterCenters(double[][] clusterCenters) {
    if (clusterCenters == null) {
      throw new IllegalArgumentException("initial cluster centers are null.");
    } else if (clusterCenters.length == 0) {
      throw new IllegalArgumentException("there are no initial cluster centers");
    } else if (clusterCenters[0] == null) {
      throw new IllegalArgumentException("Bad initial cluster centers format.");
    }
    final int dimension = clusterCenters[0].length;
    for (var clusterCenter : clusterCenters) {
      if (clusterCenter == null || clusterCenter.length != dimension) {
        throw new IllegalArgumentException("Bad initial cluster centers format.");
      }
    }
  }

  private Validator() {
    // private constructor to prevent creating objects of this class.
  }
}
