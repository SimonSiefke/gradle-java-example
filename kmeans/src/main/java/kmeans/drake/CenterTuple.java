package kmeans.drake;

public class CenterTuple {
  public double distance;
  public int index;

  public CenterTuple(double distance, int index) {
    this.distance = distance;
    this.index = index;
  }

  public CenterTuple copy() {
    return new CenterTuple(distance, index);
  }

}
