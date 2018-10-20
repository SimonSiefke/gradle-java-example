package kmeans;

/**
 * Validator for validating all kinds of stuff.
 */
public final class Validator {

  private Validator() {
    // private constructor to prevent creating objects of this class.
  }

  public static void validateDataPoints(double[][] dataPoints) {
    if (dataPoints == null) {
      throw new IllegalArgumentException("Data set is null.");
    } else if (dataPoints.length == 0) {
      throw new IllegalArgumentException("Data set is empty.");
    } else if (dataPoints[0] == null) {
      throw new IllegalArgumentException("Bad data set format.");
    }
    int dimension = dataPoints[0].length;
    int length = dataPoints.length;
    for (int i = 1; i < length; i++) {
      if (dataPoints[i] == null || dataPoints[i].length != dimension) {
        throw new IllegalArgumentException("Bad data set format.");
      }
    }
  }
}
