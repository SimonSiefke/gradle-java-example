package kmeans.elkan;

import java.util.ArrayList;
import java.util.List;

/**
 * Elkan Cluster.
 */
public class ElkanCluster {
  public double[] center;
  public final List<double[]> closestPoints = new ArrayList<>();

  public ElkanCluster(double... center) {
    this.center = center;
  }
}
