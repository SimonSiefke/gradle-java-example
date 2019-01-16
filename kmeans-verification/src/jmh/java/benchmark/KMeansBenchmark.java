package benchmark;

import kmeans.Cluster;
import kmeans.KMeansBuilder;
import kmeans.KMeansStrategy;
import org.openjdk.jmh.annotations.*;
import util.dataloader.DataLoader;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark basic kmeans Might be substituted by one class with different
 * settings for each algorithm to allow for more options to be checked without
 * creating unnecessary complexity/overhead. Useful for benching multithreaded
 * distinct from singlethreaded code
 */
@State(Scope.Benchmark)
@Fork(value = 3)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class KMeansBenchmark {

  public double[][] data;

  // TODO figure out which sets to use, the ones below are all 2d
  // "Birch1", "Birch2", "Birch3" are just too slow
  // , "dim2", "S1", "S2", "S3", "S4", "Unbalance"
  @Param({ "A2.txt", /* Small dim2 dataset */
      // "Birch1.txt", /* Large dim2 dataset */
      // "MNIST748.txt", /* Medium dim748 dataset */
      // "covtype.csv", /* Large dim55 dataset */
      "dim2.txt", /* Very small dim2 dataset */
      "dim32.txt", /* Very small dim32 dataset */
      "dim128.txt", /* Very small dim128 dataset */
      "dim512.txt", /* Very small dim512 dataset */
      "dim1024.txt" /* Very small dim1024 dataset */
  })
  public String dataSet;

  @Param({ "16", "32", "64", "128", "256" })
  public int numberOfClusters;

  public KMeansBuilder kmeans;


  // Refractor to inner state class with @State
  @Setup
  public void loadData() throws FileNotFoundException {
    data = DataLoader.TEXT("data/" + dataSet);
    kmeans = new KMeansBuilder().withDataPoints(data).withNumberOfClusters(numberOfClusters);


    /*
     * Ignored for now to test how algorithms compare with increasing k switch
     * (dataSet) { case "dim32": case "dim64": case "dim128": case "dim256": case
     * "dim512": case "dim1024": k = "16"; break; case "Birch1": case "Birch2": case
     * "Birch3": k = "100"; break; case "S1": case "S2": case "S3": case "S4": k =
     * "15"; break; case "A1": k = "20"; break; case "A2": k = "35"; break; case
     * "A3": k = "50"; break; case "dim2": k = "9"; break; case "Unbalance": k =
     * "8"; break; }
     */

  }

  @Benchmark
  public Cluster[] lloyd() {
    return kmeans.withStrategy(KMeansStrategy.LLOYD).cluster();
  }

  @Benchmark
  public Cluster[] elkan() {
    return kmeans.withStrategy(KMeansStrategy.ELKAN).cluster();
  }

}
