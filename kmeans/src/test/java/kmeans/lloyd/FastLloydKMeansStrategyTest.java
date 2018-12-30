package kmeans.lloyd;

import kmeans.KMeansStrategyTestBase;
import kmeans.lloyd.FastLloydKMeansStrategy;

public class FastLloydKMeansStrategyTest extends KMeansStrategyTestBase<FastLloydKMeansStrategy> {
  @Override
  protected FastLloydKMeansStrategy createInstance() {
    return new FastLloydKMeansStrategy();
  }
}
