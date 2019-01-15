package kmeans.lloyd;

import kmeans.KMeansStrategyTestBase;

public class FastLloydKMeansStrategyTest extends KMeansStrategyTestBase<FastLloydKMeansStrategy> {
  @Override
  protected FastLloydKMeansStrategy createInstance() {
    return new FastLloydKMeansStrategy();
  }
}
