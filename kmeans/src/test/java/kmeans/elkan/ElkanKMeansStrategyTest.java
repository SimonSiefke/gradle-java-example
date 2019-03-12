package kmeans.elkan;

import kmeans.KMeansStrategyTestBase;

public class ElkanKMeansStrategyTest extends KMeansStrategyTestBase<ElkanKMeansStrategy> {
  @Override
  protected ElkanKMeansStrategy createInstance() {
    return new ElkanKMeansStrategy();
  }
}
