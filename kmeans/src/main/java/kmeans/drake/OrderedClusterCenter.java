package kmeans.drake;

/**
 * Ordered CLuster Center.
 */
public class OrderedClusterCenter implements Comparable<OrderedClusterCenter> {
  public double distance;
  public int index;

  public OrderedClusterCenter(double distance, int index) {
    this.distance = distance;
    this.index = index;
  }

  @Override
  public int compareTo(OrderedClusterCenter other) {
    if (this.distance > other.distance) {
      return 1;
    } else if (this.distance == other.distance) {
      return 0;
    } else {
      return -1;
    }
  }
}
