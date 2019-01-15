import distance.EuclideanDistanceStrategy;
import distance.EuclideanSquaredDistanceStrategy;
import kmeans.Cluster;
import kmeans.drake.JanisDrakeKMeansStrategy;
import util.dataloader.DataLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public final class Main {
  private Main() {
    // no public constructor
  }

  public void dostuff(){
    InputStream is = getClass().getClassLoader().getResourceAsStream("file.txt");
    char[] buf = new char[100];
    try {
      InputStreamReader r = new InputStreamReader(is);
      r.read(buf, 0, 100);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    System.out.println(buf);
  }

  public static void main(String[] args) {

     var dataPoints = DataLoader.TEXT("A1.txt");
     var initialClusterCenters = new double[][] { dataPoints[0], dataPoints[1],
     dataPoints[2], dataPoints[3],
     dataPoints[4], dataPoints[5], dataPoints[6], dataPoints[7], dataPoints[8],
     dataPoints[9], dataPoints[10],
     dataPoints[11], dataPoints[12], dataPoints[13], dataPoints[14],
     dataPoints[15], dataPoints[16], dataPoints[17],
     dataPoints[18], dataPoints[19], dataPoints[20], dataPoints[21],
     dataPoints[22], dataPoints[23], dataPoints[24],
     dataPoints[25], dataPoints[26] };

    // //
    // //
     Cluster[] clusters = new JanisDrakeKMeansStrategy().cluster(dataPoints,
     initialClusterCenters, 100,
     new EuclideanDistanceStrategy());
     Arrays.sort(clusters, (clusterA, clusterB) ->
     Double.compare(clusterA.center[0], clusterB.center[0]));

    //
    //
    //

     for (var c : clusters) {
     System.out.println(Arrays.toString(c.center));

     }
    // System.out.println('\n');
  }
}