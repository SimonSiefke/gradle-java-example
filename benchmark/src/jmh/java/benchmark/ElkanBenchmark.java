// package benchmark;

// import java.util.concurrent.TimeUnit;

// import org.graalvm.compiler.lir.LIRInstruction.State;
// import org.openjdk.jmh.annotations.*;

// import dataloader.DataLoader;
// import distance.DistanceStrategy;
// import distance.EuclideanDistanceStrategy;

// @BenchmarkMode(Mode.AverageTime)
// @OutputTimeUnit(TimeUnit.NANOSECONDS)
// @State(Scope.Benchmark)
// public class ElkanBenchmark {
// private DistanceStrategy distanceStrategy = new EuclideanDistanceStrategy();
// private int numberOfIterations = Integer.MAX_VALUE;
// private double[][] data = DataLoader.TEXT("./benchmark/build/data/A1.txt");
// // private double[][] data = DataLoader.TEXT("./1.txt");
// private double[][] initialClusterCenters = new double[][] { { 1, 1 }, { 2, 2
// } };

// @Benchmark
// public void lloyd() {
// // new LloydKMeansStrategy().cluster(data, initialClusterCenters,
// // numberOfIterations, distanceStrategy);
// // File directory = new File("./bongowarongo");
// // System.out.println(directory.getAbsolutePath());
// }

// @Benchmark
// public void elkan() {
// // new ElkanKMeansStrategy().cluster(data, initialClusterCenters,
// // numberOfIterations, distanceStrategy);
// }
// }
