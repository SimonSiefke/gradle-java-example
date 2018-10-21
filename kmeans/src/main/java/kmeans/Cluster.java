package kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Cluster.
 */
public class Cluster {
  public double[] center;
  public final List<double[]> closestPoints = new ArrayList<>();

  public Cluster(double... center) {
    this.center = center;
  }
}
