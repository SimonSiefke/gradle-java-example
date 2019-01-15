package kmeans.lloyd;

import kmeans.KMeansStrategyTestBase;

public class LloydKMeansStrategyTest extends KMeansStrategyTestBase<LloydKMeansStrategy> {
  @Override
  protected LloydKMeansStrategy createInstance() {
    return new LloydKMeansStrategy();
  }
}
