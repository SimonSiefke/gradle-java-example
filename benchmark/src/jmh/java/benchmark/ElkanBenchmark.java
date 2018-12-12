package benchmark;

import distance.DistanceStrategy;
import distance.EuclideanDistanceStrategy;
import kmeans.KMeansBuilder;
import kmeans.elkan.ElkanKMeansStrategy;
import kmeans.lloyd.LloydKMeansStrategy;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import dataloader.DataLoader;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ElkanBenchmark {
  private DistanceStrategy distanceStrategy = new EuclideanDistanceStrategy();
  private int numberOfIterations = Integer.MAX_VALUE;
  private double[][] data = DataLoader.TEXT("../benchmark/data/A1.txt");
  private double[][] initialClusterCenters = new double[][] { { 1, 1 }, { 2, 2 } };

  @Benchmark
  public void lloyd() {
    new LloydKMeansStrategy().cluster(data, initialClusterCenters, numberOfIterations, distanceStrategy);
  }

  @Benchmark
  public void elkan() {
    new ElkanKMeansStrategy().cluster(data, initialClusterCenters, numberOfIterations, distanceStrategy);
  }
}
